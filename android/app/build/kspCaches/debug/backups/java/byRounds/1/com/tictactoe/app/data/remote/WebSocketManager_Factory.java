package com.tictactoe.app.data.remote;

import com.squareup.moshi.Moshi;
import com.tictactoe.app.data.local.TokenDataStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class WebSocketManager_Factory implements Factory<WebSocketManager> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  private final Provider<Moshi> moshiProvider;

  private final Provider<TokenDataStore> tokenDataStoreProvider;

  private final Provider<NetworkConfig> networkConfigProvider;

  public WebSocketManager_Factory(Provider<OkHttpClient> okHttpClientProvider,
      Provider<Moshi> moshiProvider, Provider<TokenDataStore> tokenDataStoreProvider,
      Provider<NetworkConfig> networkConfigProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
    this.moshiProvider = moshiProvider;
    this.tokenDataStoreProvider = tokenDataStoreProvider;
    this.networkConfigProvider = networkConfigProvider;
  }

  @Override
  public WebSocketManager get() {
    return newInstance(okHttpClientProvider.get(), moshiProvider.get(), tokenDataStoreProvider.get(), networkConfigProvider.get());
  }

  public static WebSocketManager_Factory create(Provider<OkHttpClient> okHttpClientProvider,
      Provider<Moshi> moshiProvider, Provider<TokenDataStore> tokenDataStoreProvider,
      Provider<NetworkConfig> networkConfigProvider) {
    return new WebSocketManager_Factory(okHttpClientProvider, moshiProvider, tokenDataStoreProvider, networkConfigProvider);
  }

  public static WebSocketManager newInstance(OkHttpClient okHttpClient, Moshi moshi,
      TokenDataStore tokenDataStore, NetworkConfig networkConfig) {
    return new WebSocketManager(okHttpClient, moshi, tokenDataStore, networkConfig);
  }
}
