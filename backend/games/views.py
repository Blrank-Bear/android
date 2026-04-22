from rest_framework import generics, permissions
from django.db.models import Q
from .models import Game
from .serializers import GameSerializer, GameHistorySerializer


class GameDetailView(generics.RetrieveAPIView):
    queryset = Game.objects.all()
    serializer_class = GameSerializer
    permission_classes = [permissions.IsAuthenticated]


class GameHistoryView(generics.ListAPIView):
    serializer_class = GameHistorySerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return Game.objects.filter(
            Q(player_x=user) | Q(player_o=user)
        ).select_related('player_x', 'player_o', 'room').order_by('-created_at')
