package com.tictactoe.app.data.remote

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConfig @Inject constructor() {
    // Change this to your server's IP/hostname when running on a real device.
    // Use 10.0.2.2 for Android emulator pointing to localhost.
    val baseUrl: String = "http://192.168.137.29:8000/"
    val wsBaseUrl: String = "ws://192.168.137.29:8000/"
}
