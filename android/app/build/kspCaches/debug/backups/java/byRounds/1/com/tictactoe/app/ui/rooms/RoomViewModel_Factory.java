package com.tictactoe.app.ui.rooms;

import com.tictactoe.app.data.repository.RoomRepository;
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
public final class RoomViewModel_Factory implements Factory<RoomViewModel> {
  private final Provider<RoomRepository> roomRepositoryProvider;

  public RoomViewModel_Factory(Provider<RoomRepository> roomRepositoryProvider) {
    this.roomRepositoryProvider = roomRepositoryProvider;
  }

  @Override
  public RoomViewModel get() {
    return newInstance(roomRepositoryProvider.get());
  }

  public static RoomViewModel_Factory create(Provider<RoomRepository> roomRepositoryProvider) {
    return new RoomViewModel_Factory(roomRepositoryProvider);
  }

  public static RoomViewModel newInstance(RoomRepository roomRepository) {
    return new RoomViewModel(roomRepository);
  }
}
