package com.tictactoe.app.data.repository;

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
public final class RoomRepository_Factory implements Factory<RoomRepository> {
  private final Provider<ApiService> apiServiceProvider;

  public RoomRepository_Factory(Provider<ApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public RoomRepository get() {
    return newInstance(apiServiceProvider.get());
  }

  public static RoomRepository_Factory create(Provider<ApiService> apiServiceProvider) {
    return new RoomRepository_Factory(apiServiceProvider);
  }

  public static RoomRepository newInstance(ApiService apiService) {
    return new RoomRepository(apiService);
  }
}
