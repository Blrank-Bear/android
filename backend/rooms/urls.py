from django.urls import path
from .views import RoomListCreateView, RoomDetailView, join_room

urlpatterns = [
    path('', RoomListCreateView.as_view(), name='room-list-create'),
    path('<int:pk>/', RoomDetailView.as_view(), name='room-detail'),
    path('<int:pk>/join/', join_room, name='room-join'),
]
