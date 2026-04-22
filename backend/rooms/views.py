from rest_framework import generics, permissions, status
from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from django.shortcuts import get_object_or_404
from .models import Room
from .serializers import RoomSerializer, RoomCreateSerializer


class RoomListCreateView(generics.ListCreateAPIView):
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        queryset = Room.objects.select_related('host', 'guest').all()
        status_filter = self.request.query_params.get('status')
        if status_filter:
            queryset = queryset.filter(status=status_filter)
        return queryset

    def get_serializer_class(self):
        return RoomCreateSerializer if self.request.method == 'POST' else RoomSerializer

    def create(self, request, *args, **kwargs):
        serializer = RoomCreateSerializer(data=request.data, context={'request': request})
        serializer.is_valid(raise_exception=True)
        room = serializer.save()
        return Response(RoomSerializer(room).data, status=status.HTTP_201_CREATED)


class RoomDetailView(generics.RetrieveAPIView):
    queryset = Room.objects.select_related('host', 'guest').all()
    serializer_class = RoomSerializer
    permission_classes = [permissions.IsAuthenticated]


@api_view(['POST'])
@permission_classes([permissions.IsAuthenticated])
def join_room(request, pk):
    room = get_object_or_404(Room, pk=pk)

    if room.status != Room.STATUS_WAITING:
        return Response({'detail': 'Room is not available to join.'}, status=400)
    if room.host == request.user:
        return Response({'detail': 'You cannot join your own room.'}, status=400)
    if room.guest is not None:
        return Response({'detail': 'Room is already full.'}, status=400)

    room.guest = request.user
    room.status = Room.STATUS_PLAYING
    room.save()

    from games.models import Game
    game = Game.objects.create(room=room, player_x=room.host, player_o=room.guest)

    return Response({'room': RoomSerializer(room).data, 'game_id': game.id})
