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
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/callback/");

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();

    private static final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
//          .state("x4xkmn9pu3j6ukrs8n")
//          .scope("user-read-birthdate,user-read-email")
//          .show_dialog(true)
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
        authorizationCodeUri_Sync();
    }


}
