package com.tictactoe.app.ui.game;

import com.tictactoe.app.data.local.TokenDataStore;
import com.tictactoe.app.data.remote.WebSocketManager;
import com.tictactoe.app.data.repository.GameRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class GameViewModel_Factory implements Factory<GameViewModel> {
  private final Provider<WebSocketManager> webSocketManagerProvider;

  private final Provider<GameRepository> gameRepositoryProvider;

  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public GameViewModel_Factory(Provider<WebSocketManager> webSocketManagerProvider,
      Provider<GameRepository> gameRepositoryProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    this.webSocketManagerProvider = webSocketManagerProvider;
    this.gameRepositoryProvider = gameRepositoryProvider;
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public GameViewModel get() {
    return newInstance(webSocketManagerProvider.get(), gameRepositoryProvider.get(), tokenDataStoreProvider.get());
  }

  public static GameViewModel_Factory create(Provider<WebSocketManager> webSocketManagerProvider,
      Provider<GameRepository> gameRepositoryProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    return new GameViewModel_Factory(webSocketManagerProvider, gameRepositoryProvider, tokenDataStoreProvider);
  }

  public static GameViewModel newInstance(WebSocketManager webSocketManager,
      GameRepository gameRepository, TokenDataStore tokenDataStore) {
    return new GameViewModel(webSocketManager, gameRepository, tokenDataStore);
  }
}
