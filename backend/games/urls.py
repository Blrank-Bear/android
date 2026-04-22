from django.urls import path
from .views import GameDetailView, GameHistoryView

urlpatterns = [
    path('history/', GameHistoryView.as_view(), name='game-history'),
    path('<int:pk>/', GameDetailView.as_view(), name='game-detail'),
]
