from django.contrib import admin
from .models import Game, Move


@admin.register(Game)
class GameAdmin(admin.ModelAdmin):
    list_display = ('room', 'player_x', 'player_o', 'result', 'created_at')
    list_filter = ('result',)


@admin.register(Move)
class MoveAdmin(admin.ModelAdmin):
    list_display = ('game', 'player', 'symbol', 'position', 'timestamp')
