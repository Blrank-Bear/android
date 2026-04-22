package com.tictactoe.app.data.remote;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class NetworkConfig_Factory implements Factory<NetworkConfig> {
  @Override
  public NetworkConfig get() {
    return newInstance();
  }

  public static NetworkConfig_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NetworkConfig newInstance() {
    return new NetworkConfig();
  }

  private static final class InstanceHolder {
    private static final NetworkConfig_Factory INSTANCE = new NetworkConfig_Factory();
  }
}
