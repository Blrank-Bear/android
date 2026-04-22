package com.tictactoe.app.data.repository;

import com.tictactoe.app.data.local.TokenDataStore;
import com.tictactoe.app.data.remote.ApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<ApiService> apiServiceProvider;

  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public AuthRepository_Factory(Provider<ApiService> apiServiceProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(apiServiceProvider.get(), tokenDataStoreProvider.get());
  }

  public static AuthRepository_Factory create(Provider<ApiService> apiServiceProvider,
      Provider<TokenDataStore> tokenDataStoreProvider) {
    return new AuthRepository_Factory(apiServiceProvider, tokenDataStoreProvider);
  }

  public static AuthRepository newInstance(ApiService apiService, TokenDataStore tokenDataStore) {
    return new AuthRepository(apiService, tokenDataStore);
  }
}
