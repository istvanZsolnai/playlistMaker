package com.codecool.spotify.playlist.controller;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.AudioAnalysis;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.albums.GetAlbumRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@Data
public class SpotifyApiController {

    static final String clientId = "313ae5eea45846098465c37b896f6b90";
    static final String clientSecret = "072d6480d9f74e87a066250de4ca2070";
    private static String accessToken = "BQA6dxXGvRf5kJ-UIlLkoTSvvFQnFU5AnUdaqXODY1PxWYOXCxifiGzKGTPcMsJK7xDR4cumASR3qpeYrkaIuJThnPq-Oye-FzRZs3TpokNK4Kn0ktm6hDn-7x0p6qcBmSfJ_FsaaKRUywoLd25gpjm5o8v19dsstDCYAmBcuOv6Hr-KqNabbrxx4YV5ITYeIkc1KRTqu7Fl22ynEDWU4A";
    private static String refreshToken = "AQDbyRvRQJwzpTNA2JCjdNz5CfUB2vHjsldukzQGy9l7yfc_FGzWgYORLZZzjF827PXzWsyvqq3cxwsOOb6K_3V34qxZkrTAuUb07Y7JGjX2MRs9XViU9bsjcvuUeFov_jVoKA";
    private static final String userId = "11124248365";

    //BQAJl5GsR3YHFWjoKOraSoqXg2zOdiVvVRgUrPFYMfuT5e_EnsAFvOl1JFyRTwKvhxnYvgyYoXF2es1RBrD8J259YYOKAYVYlhjylJtA2gvaz4inV4gGbZHB8ErB9XQMoNu0nkRAGBP5eeW433_GlDlH-qFjfg9jnOP67Z383u9u2hTGUtcnjIXQDne5jKFEptuPtn1ClzXyNxszGdGs4w

    SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setAccessToken(accessToken)
            //.setRefreshToken("AQDbyRvRQJwzpTNA2JCjdNz5CfUB2vHjsldukzQGy9l7yfc_FGzWgYORLZZzjF827PXzWsyvqq3cxwsOOb6K_3V34qxZkrTAuUb07Y7JGjX2MRs9XViU9bsjcvuUeFov_jVoKA")
            .build();

    @GetMapping("/get")
    private Album getTest() throws IOException, SpotifyWebApiException {

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

        return kamikazeAlbum;
    }

    @GetMapping("/createPlaylist/{name}")
    private void createPlayList(@PathVariable("name") String name) throws IOException, SpotifyWebApiException {
        System.out.println("Started creating playlist");
        CreatePlaylistRequest testPlaylist = spotifyApi.createPlaylist("11124248365", name).build();
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

    @GetMapping("/recent")
    @ResponseBody
    private PlayHistory[] getRecentlyPlayed() throws IOException, SpotifyWebApiException {
       PagingCursorbased<PlayHistory> recentlyPlayed = spotifyApi.getCurrentUsersRecentlyPlayedTracks().build().execute();
       return recentlyPlayed.getItems();
    }

    @GetMapping("/getPlaylists")
    @ResponseBody
    private Paging<PlaylistSimplified> getUserPlaylists() throws IOException, SpotifyWebApiException {
        Paging<PlaylistSimplified> userPlaylist = spotifyApi.getListOfUsersPlaylists(userId).build().execute();
        PlaylistSimplified[] listOfPlaylists = userPlaylist.getItems();
        return userPlaylist;
    }

    @GetMapping("/getPlaylistID")
    @ResponseBody
    private String getPlayListID(String playListName) throws IOException, SpotifyWebApiException {
        Paging<PlaylistSimplified> playLists = getUserPlaylists();
        PlaylistSimplified[] items = playLists.getItems();
        String playListId = "";
        for (PlaylistSimplified item : items) {
            if (item.getName().equals(playListName)){
                System.out.println(item.getId());
                playListId = item.getId();
            }
        }
        return playListId;
    }

    @GetMapping("/recommend")
    @ResponseBody
    private TrackSimplified[] getReccommendations() throws IOException, SpotifyWebApiException {
        //Recommendations recommendations = spotifyApi.getRecommendations().build().execute();
        Recommendations recommendations = spotifyApi.getRecommendations().build().execute();
        System.out.println(recommendations.getTracks());
        //TrackSimplified[] tracks = recommendations.getTracks();
        //System.out.println(tracks);
        return null;
    }

    @GetMapping("/addSongToPlaylist")
    private void addSongToPlaylist(String[] songId, String playListName) throws IOException, SpotifyWebApiException {
        String playListId = getPlayListID("TestTestTest");
        spotifyApi.addTracksToPlaylist(playListId, new String[]{"spotify:track:51o6QcfrtUXKhZn2qlCyMW"}).build().execute();
        System.out.println("Song Added To Playlist");
    }

    @GetMapping("/get-analysis")
    private AudioAnalysis getAudioAnalysis() throws IOException, SpotifyWebApiException {
       return spotifyApi.getAudioAnalysisForTrack("spotify:track:26douMAqNELour6sKd2oR7").build().execute();
    }
}
