from django.contrib import admin
from .models import Room


@admin.register(Room)
class RoomAdmin(admin.ModelAdmin):
    list_display = ('name', 'host', 'guest', 'status', 'created_at')
    list_filter = ('status',)
