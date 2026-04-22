package com.tictactoe.app.ui.history;

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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<GameRepository> gameRepositoryProvider;

  public HistoryViewModel_Factory(Provider<GameRepository> gameRepositoryProvider) {
    this.gameRepositoryProvider = gameRepositoryProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(gameRepositoryProvider.get());
  }

  public static HistoryViewModel_Factory create(Provider<GameRepository> gameRepositoryProvider) {
    return new HistoryViewModel_Factory(gameRepositoryProvider);
  }

  public static HistoryViewModel newInstance(GameRepository gameRepository) {
    return new HistoryViewModel(gameRepository);
  }
}
