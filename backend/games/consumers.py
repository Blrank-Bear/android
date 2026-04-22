import json
from channels.generic.websocket import AsyncWebsocketConsumer
from channels.db import database_sync_to_async
from django.utils import timezone
from .models import Game, Move
from .game_logic import check_winner, apply_move, is_valid_move


class GameConsumer(AsyncWebsocketConsumer):

    async def connect(self):
        self.room_id = self.scope['url_route']['kwargs']['room_id']
        self.group_name = f'game_{self.room_id}'
        await self.channel_layer.group_add(self.group_name, self.channel_name)
        await self.accept()

        game = await self.get_game()
        if game:
            await self.send(text_data=json.dumps({
                'type': 'game_state',
                'board': game['board'],
                'current_turn': game['current_turn'],
                'result': game['result'],
                'player_x': game['player_x'],
                'player_o': game['player_o'],
            }))

    async def disconnect(self, close_code):
        await self.channel_layer.group_discard(self.group_name, self.channel_name)

    async def receive(self, text_data):
        data = json.loads(text_data)
        action = data.get('action')

        if action == 'make_move':
            await self.handle_move(data)
        elif action == 'rematch':
            await self.handle_rematch()

    # ── make_move ─────────────────────────────────────────────────────────────

    async def handle_move(self, data):
        user = self.scope['user']
        if not user.is_authenticated:
            await self.send_error('Authentication required.')
            return

        result = await self.process_move(user.id, data.get('position'))
        if result.get('error'):
            await self.send_error(result['error'])
            return

        await self.channel_layer.group_send(self.group_name, {
            'type': 'game_update',
            'board': result['board'],
            'current_turn': result['current_turn'],
            'result': result['result'],
            'last_move': {
                'position': data.get('position'),
                'symbol': result['symbol'],
            },
            'winning_line': result.get('winning_line'),
        })

    async def game_update(self, event):
        await self.send(text_data=json.dumps({
            'type': 'game_state',
            'board': event['board'],
            'current_turn': event['current_turn'],
            'result': event['result'],
            'last_move': event.get('last_move'),
            'winning_line': event.get('winning_line'),
        }))

    # ── rematch ───────────────────────────────────────────────────────────────

    async def handle_rematch(self):
        user = self.scope['user']
        if not user.is_authenticated:
            await self.send_error('Authentication required.')
            return

        result = await self.process_rematch(user.id)
        if result.get('error'):
            await self.send_error(result['error'])
            return

        # Broadcast fresh game state to both players
        await self.channel_layer.group_send(self.group_name, {
            'type': 'game_update',
            'board': result['board'],
            'current_turn': result['current_turn'],
            'result': result['result'],
            'last_move': None,
            'winning_line': None,
        })

    # ── error helper ──────────────────────────────────────────────────────────

    async def send_error(self, message):
        await self.send(text_data=json.dumps({'type': 'error', 'message': message}))

    # ── DB helpers ────────────────────────────────────────────────────────────

    @database_sync_to_async
    def get_game(self):
        try:
            game = Game.objects.select_related('player_x', 'player_o').get(
                room_id=self.room_id
            )
            return {
                'board': game.board_state,
                'current_turn': game.current_turn,
                'result': game.result,
                'player_x': game.player_x.username,
                'player_o': game.player_o.username,
            }
        except Game.DoesNotExist:
            return None

    @database_sync_to_async
    def process_move(self, user_id, position):
        try:
            game = Game.objects.select_related('player_x', 'player_o', 'room').get(
                room_id=self.room_id
            )
        except Game.DoesNotExist:
            return {'error': 'Game not found.'}

        if game.result != Game.RESULT_IN_PROGRESS:
            return {'error': 'Game is already finished.'}

        if game.player_x_id == user_id:
            symbol = 'X'
        elif game.player_o_id == user_id:
            symbol = 'O'
        else:
            return {'error': 'You are not a player in this game.'}

        if game.current_turn != symbol:
            return {'error': 'It is not your turn.'}

        if not is_valid_move(game.board_state, position):
            return {'error': 'Invalid move.'}

        new_board = apply_move(game.board_state, position, symbol)
        game.board_state = new_board
        Move.objects.create(game=game, player_id=user_id, position=position, symbol=symbol)

        winner = check_winner(new_board)
        winning_line = None

        if winner == 'draw':
            game.result = Game.RESULT_DRAW
            game.finished_at = timezone.now()
            game.room.status = 'finished'
            game.room.save()
        elif winner:
            game.result = winner
            game.finished_at = timezone.now()
            game.room.status = 'finished'
            game.room.save()
            winning_line = get_winning_line(new_board)
        else:
            game.current_turn = 'O' if symbol == 'X' else 'X'

        game.save()
        return {
            'board': game.board_state,
            'current_turn': game.current_turn,
            'result': game.result,
            'symbol': symbol,
            'winning_line': winning_line,
        }

    @database_sync_to_async
    def process_rematch(self, user_id):
        try:
            game = Game.objects.select_related('player_x', 'player_o', 'room').get(
                room_id=self.room_id
            )
        except Game.DoesNotExist:
            return {'error': 'Game not found.'}

        # Only players in this game can request a rematch
        if game.player_x_id != user_id and game.player_o_id != user_id:
            return {'error': 'You are not a player in this game.'}

        if game.result == Game.RESULT_IN_PROGRESS:
            return {'error': 'Game is still in progress.'}

        # Reset the game — swap sides so the loser goes first as X
        old_x = game.player_x
        old_o = game.player_o
        game.player_x = old_o
        game.player_o = old_x
        game.board_state = ' ' * 9
        game.current_turn = 'X'
        game.result = Game.RESULT_IN_PROGRESS
        game.finished_at = None
        game.room.status = 'playing'
        game.room.save()
        game.save()

        return {
            'board': game.board_state,
            'current_turn': game.current_turn,
            'result': game.result,
        }


def get_winning_line(board: str):
    """Return the list of 3 positions that form the winning line, or None."""
    from .game_logic import WINNING_COMBINATIONS
    for combo in WINNING_COMBINATIONS:
        a, b, c = combo
        if board[a] != ' ' and board[a] == board[b] == board[c]:
            return list(combo)
    return None
