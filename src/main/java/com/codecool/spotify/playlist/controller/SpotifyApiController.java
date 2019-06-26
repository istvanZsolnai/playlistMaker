package com.codecool.spotify.playlist.controller;

import com.codecool.spotify.playlist.service.TrackService;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.AudioAnalysis;
import com.wrapper.spotify.model_objects.miscellaneous.Device;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.albums.GetAlbumRequest;
import com.wrapper.spotify.requests.data.artists.GetArtistRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@Data
public class SpotifyApiController {

    @Value("${client.id}")
    final String clientId = null;

    @Value("${client.secret}")
    final String clientSecret = null;

    @Value("${refresh.token}")
    private String refreshToken;

    @Value("${user.id}")
    private String userId;

//    @Value("${access.token}")
//    private String accessToken;

    private String accessToken = "BQDIfHcU5OfQf4Xfu-VcKXUB_7WOYDrJIjMy3pkMjhK_B_YDCOAitahOy-uSTe_iMv2iNFCC7jmSdR6sGM48r6GL5R1yTWI7zH1leM-xffCa_b0v_wyeKujQdB3lImNtgKei4QYW7CgkbzxYI-1r8k1biVteOekXCRuwFzzWWofkvp2WUo2JbMM61Tt2lfBAjzdSnFy6b7jcDe-hP2-B2NAyFKzDh3A";

    private String songNameGlobal;

    @Autowired
    TrackService trackService;

    private SpotifyApi spotifyApi = new SpotifyApi.Builder()
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
    private Recommendations getReccommendations() throws IOException, SpotifyWebApiException {
        Recommendations recommendations = spotifyApi.getRecommendations().build().execute();
        System.out.println(recommendations.getTracks());
        return recommendations;
    }

    @GetMapping("/addSongToPlaylist")
    private void addSongToPlaylist(String[] songId, String playListName) throws IOException, SpotifyWebApiException {
        String playListId = getPlayListID("TestTestTest");
        spotifyApi.addTracksToPlaylist(playListId, new String[]{"spotify:track:51o6QcfrtUXKhZn2qlCyMW"}).build().execute();
        System.out.println("Song Added To Playlist");
    }

    @GetMapping("/get-analysis")
    private AudioAnalysis getAudioAnalysis() throws IOException, SpotifyWebApiException {
      //  https://api.spotify.com/v1/audio-analysis/{B9GAvKqBTmSnayKSf08PBA}
       AudioAnalysis analysis = spotifyApi.getAudioAnalysisForTrack("B9GAvKqBTmSnayKSf08PBA").build().execute();
        System.out.println(analysis.getTrack());
        return analysis;
    }

    @GetMapping("/start")
    private String startMusic() throws IOException, SpotifyWebApiException {
        spotifyApi.startResumeUsersPlayback().build().execute();
        System.out.println("Music started");
        return "Started";
    }

    @GetMapping("/pause")
    private void stopMusic() throws IOException, SpotifyWebApiException {
        spotifyApi.pauseUsersPlayback().build().execute();
    }

    @GetMapping("/devices")
    @ResponseBody
    private Device[] getUsersAvailableDevices() throws IOException, SpotifyWebApiException {
        Device[] devices = spotifyApi.getUsersAvailableDevices().build().execute();
        return devices;
    }

    @GetMapping("/song/{name}")
    @ResponseBody
    public Paging<Track> getTracks(@PathVariable("name") String songName) throws IOException, SpotifyWebApiException {
        songNameGlobal = songName;
        Paging<Track> response = spotifyApi.searchTracks(songName).limit(1).build().execute();
       return response;
    }
//
//    @GetMapping("/song/{name}")
//    @ResponseBody
//    public void getTrack() throws IOException, SpotifyWebApiException {
//        //return spotifyApi.getTrack(songNameGlobal).build().execute();
//    }

    @GetMapping("/song-name")
    @ResponseBody
    public void getSongName() throws IOException, SpotifyWebApiException {
        //trackService.getRelevantDataFromTrack(getTracks(songNameGlobal))
        trackService.getRelevanData();
        System.out.println("Here I am");

    }
}
