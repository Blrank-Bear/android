from django.db import models
from django.conf import settings


class Game(models.Model):
    RESULT_IN_PROGRESS = 'in_progress'
    RESULT_X_WINS = 'X'
    RESULT_O_WINS = 'O'
    RESULT_DRAW = 'draw'

    RESULT_CHOICES = [
        (RESULT_IN_PROGRESS, 'In Progress'),
        (RESULT_X_WINS, 'X Wins'),
        (RESULT_O_WINS, 'O Wins'),
        (RESULT_DRAW, 'Draw'),
    ]

    room = models.OneToOneField('rooms.Room', on_delete=models.CASCADE, related_name='game')
    player_x = models.ForeignKey(
        settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='games_as_x'
    )
    player_o = models.ForeignKey(
        settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='games_as_o'
    )
    # 9-char string: ' ', 'X', or 'O'
    board_state = models.CharField(max_length=9, default=' ' * 9)
    current_turn = models.CharField(max_length=1, default='X')
    result = models.CharField(max_length=11, choices=RESULT_CHOICES, default=RESULT_IN_PROGRESS)
    created_at = models.DateTimeField(auto_now_add=True)
    finished_at = models.DateTimeField(null=True, blank=True)

    def __str__(self):
        return f'Game in {self.room.name} — {self.result}'


class Move(models.Model):
    game = models.ForeignKey(Game, on_delete=models.CASCADE, related_name='moves')
    player = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    position = models.IntegerField()
    symbol = models.CharField(max_length=1)
    timestamp = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ['timestamp']

    def __str__(self):
        return f'{self.player.username} played {self.symbol} at {self.position}'
