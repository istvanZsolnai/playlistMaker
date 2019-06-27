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
import java.util.LinkedList;
import java.util.List;

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

    private String accessToken = "BQAuQocQ3UOS49k0hu_Urq2mXToTVakQ9QMuPy9vQwkAH1wBTddx6cylby2M0pfw0wb1iXLonHSKK6SKBDgdJNLFpZB5vqoDZV5CzVLAVifQ7_fFzjMAufwx_eMy5I4QJPTxqyH2o26GxxdPac8C8y5mg356Wd_7bvpZ-2GgBVVtQ1pObVns6h35oqxmC04mHyDPsGhewJ9KyH5_mCcHbsLqn7jVg80";

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
    public List<String> getReccommendations() throws IOException, SpotifyWebApiException {
        List<String> listOfReccomendations = getRecommendationsBasedOnRecentlyListened();
        System.out.println(listOfReccomendations);

        Recommendations recommendations = spotifyApi.getRecommendations().seed_tracks(listOfReccomendations.get(0))
                                                                        .seed_tracks(listOfReccomendations.get(1))
                                                                        .seed_tracks(listOfReccomendations.get(2))
                                                                        .seed_tracks(listOfReccomendations.get(3)).limit(1).build().execute();
        System.out.println(recommendations.getTracks().length);
        TrackSimplified[] recommendedTracks = recommendations.getTracks();
        List<String> listOfTrackNamesRecommended = new LinkedList<>();
        for (TrackSimplified recom: recommendedTracks
             ) {listOfTrackNamesRecommended.add(recom.getName());
            System.out.println(recom.getName() + "HEREE");

        }
        return listOfTrackNamesRecommended;
    }
    @GetMapping("/addRecommended/{playListName}")
    @ResponseBody
    private String addRecommendedSongsToPlaylist(@PathVariable("playListName")String playlistName) throws IOException, SpotifyWebApiException {
        createPlayList(playlistName);
        for (int i = 0; i < 10 ; i++) {
            List<String> recommendedSong = getReccommendations();
            addSongToPlaylist(String.valueOf(recommendedSong), playlistName);
        }
        return "Songs Added";

    }

    @GetMapping("/recently")
    @ResponseBody
    public List<String> getRecommendationsBasedOnRecentlyListened() throws IOException, SpotifyWebApiException {
        PagingCursorbased<PlayHistory> playHistory = spotifyApi.getCurrentUsersRecentlyPlayedTracks().limit(5).build().execute();
        PlayHistory[] playHistoryItem = playHistory.getItems();
        List<String> trackList = new LinkedList<>();
        for (PlayHistory item:playHistoryItem
             ) {
            String listEelement = item.getTrack().getUri();
            String[] splitElement = listEelement.split(":");
            trackList.add(splitElement[2]);
            System.out.println(splitElement[2]);
        }
        return trackList;

    }

    @GetMapping("/addSongToPlaylist/{name}")
    private void addSongToPlaylist(@PathVariable("name")String songName, String playListName) throws IOException, SpotifyWebApiException {
        String playListId = getPlayListID(playListName);
        String[] songID = getTrackURI(songName);
        spotifyApi.addTracksToPlaylist(playListId, songID).build().execute();
        System.out.println("Song Added To Playlist");
    }

    @GetMapping("/getAnalysis")
    @ResponseBody
    public AudioAnalysis getAudioAnalysis() throws IOException, SpotifyWebApiException {
        System.out.println();
       AudioAnalysis analysis = spotifyApi.getAudioAnalysisForTrack("01iyCAUm8EvOFqVWYJ3dVX").build().execute();
        System.out.println(analysis.getTrack().getDuration());
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
        getTrackId(response);
       return response;
    }


    public String getTrackId(Paging<Track> track){
        Track[] trackItems = track.getItems();
        System.out.println(trackItems);
        String trackID = "";
        for (Track trackInList: trackItems
             ) {
             trackID = trackInList.getId();

        }
        //track1.id = track.getItems();
        System.out.println(trackID);
        return trackID;
    }

    public String[] getTrackId(String songName) throws IOException, SpotifyWebApiException {
       Paging<Track> response = spotifyApi.searchTracks(songName).limit(1).build().execute();
        String trackID = "";
        String [] trackIDList = new String[1];
        Track[] trackItems = response.getItems();
        for (Track trackInList:trackItems
             ) {
            trackID = trackInList.getId();
            trackIDList[0] = trackID;
        }
        return trackIDList;

    }

    public String[] getTrackURI(String songName) throws IOException, SpotifyWebApiException {
        Paging<Track> response = spotifyApi.searchTracks(songName).limit(1).build().execute();
        String trackURI = "";
        String [] trackURIList = new String[1];
        Track[] trackItems = response.getItems();
        for (Track trackInList:trackItems
        ) {
            trackURI = trackInList.getUri();
            trackURIList[0] = trackURI;
        }
        return trackURIList;
    }

}
