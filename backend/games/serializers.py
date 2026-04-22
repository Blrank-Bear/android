from rest_framework import serializers
from accounts.serializers import UserSerializer
from rooms.serializers import RoomSerializer
from .models import Game, Move


class MoveSerializer(serializers.ModelSerializer):
    player = UserSerializer(read_only=True)

    class Meta:
        model = Move
        fields = ('id', 'player', 'position', 'symbol', 'timestamp')


class GameSerializer(serializers.ModelSerializer):
    player_x = UserSerializer(read_only=True)
    player_o = UserSerializer(read_only=True)
    room = RoomSerializer(read_only=True)
    moves = MoveSerializer(many=True, read_only=True)

    class Meta:
        model = Game
        fields = (
            'id', 'room', 'player_x', 'player_o',
            'board_state', 'current_turn', 'result',
            'created_at', 'finished_at', 'moves'
        )


class GameHistorySerializer(serializers.ModelSerializer):
    player_x = UserSerializer(read_only=True)
    player_o = UserSerializer(read_only=True)
    room_name = serializers.CharField(source='room.name', read_only=True)

    class Meta:
        model = Game
        fields = ('id', 'room_name', 'player_x', 'player_o', 'result', 'created_at', 'finished_at')
