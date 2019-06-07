package com.codecool.spotify.playlist.controller;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.albums.GetAlbumRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@Data
public class SpotifyApiController {

    static final String clientId = "313ae5eea45846098465c37b896f6b90";
    static final String clientSecret = "072d6480d9f74e87a066250de4ca2070";
    private static String accessToken = "BQDf6ifsYzqRWFUXxgwjf0wuvo9TL2bESmLGqN-fh9HhoDsVJK_vW2U3aWkSsCxGh0aiJiGPbuqkN7meLdGV89idKjJj_iNQkyCIsd4NlIjN_15C9KLzjfZjJs8Vzyv0SUFw7elEmoAKXrlRMTUUwbv2F6Bx3tXLhItQerDXxqMrh2-36XXvZNz7NJY-1cSsTrsgA58igxQE-scYIVriHA";
    private static String refreshToken = "AQDbyRvRQJwzpTNA2JCjdNz5CfUB2vHjsldukzQGy9l7yfc_FGzWgYORLZZzjF827PXzWsyvqq3cxwsOOb6K_3V34qxZkrTAuUb07Y7JGjX2MRs9XViU9bsjcvuUeFov_jVoKA";
    private static final String userId = "11124248365";

    SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setAccessToken(accessToken)
            .build();

    @GetMapping("/get")
    private Artist getTest() throws IOException, SpotifyWebApiException {

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
    @GetMapping("/createPlaylist")
    private void createPlayList() throws IOException, SpotifyWebApiException {
        CreatePlaylistRequest testPlaylist = spotifyApi.createPlaylist("11124248365", "TEst Playlist").build();
        Playlist playlist = testPlaylist.execute();
        System.out.println(playlist.getName());
    }

    @GetMapping("/top")
    @ResponseBody
    private Artist[] getTopArtists() throws IOException, SpotifyWebApiException {
        GetUsersTopArtistsRequest topArtist = spotifyApi.getUsersTopArtists().build();
        System.out.println(spotifyApi.getUsersTopArtists().toString());
        Paging<Artist> topArtistsExecuted = topArtist.execute();
        System.out.println(topArtistsExecuted.getTotal());
        return topArtistsExecuted.getItems();
    }

    @GetMapping("recent")
    @ResponseBody
    private PlayHistory[] getRecentlyPlayed() throws IOException, SpotifyWebApiException {
       PagingCursorbased<PlayHistory> recentlyPlayed = spotifyApi.getCurrentUsersRecentlyPlayedTracks().build().execute();
       return recentlyPlayed.getItems();
    }

    @GetMapping("/get2")
    private void getAlbum() {
    }
}
