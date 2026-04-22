from rest_framework import serializers
from accounts.serializers import UserSerializer
from .models import Room


class RoomSerializer(serializers.ModelSerializer):
    host = UserSerializer(read_only=True)
    guest = UserSerializer(read_only=True)

    class Meta:
        model = Room
        fields = ('id', 'name', 'host', 'guest', 'status', 'created_at')
        read_only_fields = ('id', 'host', 'guest', 'status', 'created_at')


class RoomCreateSerializer(serializers.ModelSerializer):
    class Meta:
        model = Room
        fields = ('name',)

    def create(self, validated_data):
        return Room.objects.create(host=self.context['request'].user, **validated_data)
