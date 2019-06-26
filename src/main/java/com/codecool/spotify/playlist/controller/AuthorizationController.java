package com.codecool.spotify.playlist.controller;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@Data
public class AuthorizationController {

    private static final String clientId = "313ae5eea45846098465c37b896f6b90";
    private static final String clientSecret = "072d6480d9f74e87a066250de4ca2070";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8888/callback");
    private static String accessToken = "BQAt2Zvp3xtUEcKjMNWJUKIp4EHLfDxv_kdAbpkWNbcCDTOne2PZyzSem9ULWJeXCwcyxP7zi0UqSRkcHvk73H67Wq8qqR3L3iyjzZD";
    private static String refreshToken = "AQAZmlY2NuxsglODAiBhu9LIfw7OQEy1nZ7lIsrHFnC9_L5Wb4zFQVqdzEbvoiWnusw8asZYgpdvA17v_zIA";
    private static final String userId = "11124248365";


    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .setAccessToken(accessToken)
            .setRefreshToken(refreshToken)
            .build();

    private static final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
          .state("x4xkmn9pu3j6ukrs8n")
          .scope("user-read-private user-read-email user-read-recently-played playlist-read-private playlist-read-private playlist-modify-public user-top-read streaming user-modify-playback-state user-read-playback-state")
          .show_dialog(true)
            .build();

    public static void authorizationCodeUri_Sync() {
        final URI uri = authorizationCodeUriRequest.execute();
        System.out.println("asdadasdasfa");
        System.out.println("URI: " + uri.toString());
    }

    public static void authorizationCodeUri_Async() {
        try {
            final Future<URI> uriFuture = authorizationCodeUriRequest.executeAsync();

            // ...

            final URI uri = uriFuture.get();

            System.out.println("URI: " + uri.toString());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        }
    }

    @GetMapping("/uri")
    public void controller(){
        authorizationCodeUri_Async();
    }


}
