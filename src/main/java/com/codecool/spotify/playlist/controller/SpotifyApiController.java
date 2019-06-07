package com.codecool.spotify.playlist.controller;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Album;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.requests.data.albums.GetAlbumRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@Data
public class SpotifyApiController {

    static final String clientId = "313ae5eea45846098465c37b896f6b90";
    static final String clientSecret = "072d6480d9f74e87a066250de4ca2070";
    static final String accesToken = "BQAeCdwc4tFa2lQpLnGl0Gu60ZpxdrZeZxwQ_wB9Ok0tVVed0WnEvCAJ0HUExKkTtyd-NJ5eVIPCU5vDFp4";


    SpotifyApi spotifyApi = new SpotifyApi.Builder().setClientId("313ae5eea45846098465c37b896f6b90").setClientSecret("072d6480d9f74e87a066250de4ca2070").build();

    @GetMapping("/get")
    private Artist getTest() throws IOException, SpotifyWebApiException {

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setAccessToken(accesToken).build();

        GetArtistRequest eminem = spotifyApi.getArtist("7dGJo4pcD2V6oG8kP0tJRR").build();

        Artist artistEminem = eminem.execute();
        System.out.println("ASDASD");
        System.out.println(artistEminem.getName());


        GetAlbumRequest kamikaze = spotifyApi.getAlbum("3HNnxK7NgLXbDoxRZxNWiR").build();
        Album kamikazeAlbum = kamikaze.execute();
        System.out.println(kamikazeAlbum.getName());
        System.out.println(kamikazeAlbum.getGenres());
        System.out.println(kamikazeAlbum.getPopularity());
        System.out.println(kamikazeAlbum.getTracks());

        return artistEminem;
    }

    @GetMapping("/get2")
    private void getAlbum() {
    }
}
