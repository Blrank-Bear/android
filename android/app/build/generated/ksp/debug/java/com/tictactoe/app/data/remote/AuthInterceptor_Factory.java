package com.tictactoe.app.data.remote;

import com.tictactoe.app.data.local.TokenDataStore;
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
public final class AuthInterceptor_Factory implements Factory<AuthInterceptor> {
  private final Provider<TokenDataStore> tokenDataStoreProvider;

  public AuthInterceptor_Factory(Provider<TokenDataStore> tokenDataStoreProvider) {
    this.tokenDataStoreProvider = tokenDataStoreProvider;
  }

  @Override
  public AuthInterceptor get() {
    return newInstance(tokenDataStoreProvider.get());
  }

  public static AuthInterceptor_Factory create(Provider<TokenDataStore> tokenDataStoreProvider) {
    return new AuthInterceptor_Factory(tokenDataStoreProvider);
  }

  public static AuthInterceptor newInstance(TokenDataStore tokenDataStore) {
    return new AuthInterceptor(tokenDataStore);
  }
}
