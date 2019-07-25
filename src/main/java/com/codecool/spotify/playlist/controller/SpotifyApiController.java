package com.codecool.spotify.playlist.controller;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.AudioAnalysis;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.miscellaneous.Device;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.text.AttributedString;
import java.util.*;

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

    private String playlistName;


    @Autowired
    private AuthorizationController authorizationController = new AuthorizationController();

    private String accessToken = "BQC0PNR0Jx-MeVz5ZBBielJfRs1XdxIwsS7Lusg8WxhAvQvwQfa4gRKW41aBD0tTDAAXREXT-ey26DdhySdi5GsA6oessx51P6A08YAKsFZB4GCFM5lZPs8DBhq3Lg9Jg0Yqs7AII-EtVSy5uEu2o0uqQuxe4uG6bZ66blCNN-nOu5gbDjO2DeM4F-V76JUTDBo0VJuz9ZS-l58FRPIImE5hmXQ-HFk";

    private SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setAccessToken(accessToken)
            //.setRefreshToken("AQDbyRvRQJwzpTNA2JCjdNz5CfUB2vHjsldukzQGy9l7yfc_FGzWgYORLZZzjF827PXzWsyvqq3cxwsOOb6K_3V34qxZkrTAuUb07Y7JGjX2MRs9XViU9bsjcvuUeFov_jVoKA")
            .build();

    @GetMapping("/")
    public String homePage(){
        return "index.html";
    }

    @GetMapping("/current")
    @ResponseBody
    public String getCurrentSong() throws IOException, SpotifyWebApiException {
        CurrentlyPlayingContext currentSongData = spotifyApi.getInformationAboutUsersCurrentPlayback().build().execute();
        return currentSongData.getItem().getId();
    }

    @GetMapping("/createPlaylist/{name}")
    private void createPlayList(@PathVariable("name") String name) throws IOException, SpotifyWebApiException {
        CreatePlaylistRequest testPlaylist = spotifyApi.createPlaylist("11124248365", name).build();
        Playlist playlist = testPlaylist.execute();
    }

    @GetMapping("/top")
    @ResponseBody
    private Artist[] getTopArtists() throws IOException, SpotifyWebApiException {
        GetUsersTopArtistsRequest topArtist = spotifyApi.getUsersTopArtists().build();
        Paging<Artist> topArtistsExecuted = topArtist.execute();
        return topArtistsExecuted.getItems();
    }

    @GetMapping("/getPlaylists")
    @ResponseBody
    private Paging<PlaylistSimplified> getUserPlaylists() throws IOException, SpotifyWebApiException {
        Paging<PlaylistSimplified> userPlaylist = spotifyApi.getListOfUsersPlaylists(userId).build().execute();
        PlaylistSimplified[] listOfPlaylists = userPlaylist.getItems();
        for (PlaylistSimplified playlist:listOfPlaylists) {
            playlist.getName();
        }
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
                playListId = item.getId();
            }
        }
        return playListId;
    }

    @GetMapping("/recommend")
    @ResponseBody
    public List<String> getReccommendations() throws IOException, SpotifyWebApiException {
        List<String> listOfReccomendations = getRecommendationsBasedOnRecentlyListened();
        Recommendations recommendations = spotifyApi.getRecommendations().seed_tracks(listOfReccomendations.get(0))
                                                                        .seed_tracks(listOfReccomendations.get(1))
                                                                        .seed_tracks(listOfReccomendations.get(2))
                                                                        .seed_tracks(listOfReccomendations.get(3))
                                                                        .seed_tracks(listOfReccomendations.get(4))
                                                                        .limit(1).build().execute();
        TrackSimplified[] recommendedTracks = recommendations.getTracks();
        List<String> listOfTrackNamesRecommended = new LinkedList<>();
        for (TrackSimplified recom: recommendedTracks
             ) {listOfTrackNamesRecommended.add(recom.getName());
        }
        return listOfTrackNamesRecommended;
    }

    @GetMapping("/addRecommended")
    public RedirectView addRecommendedSongsToPlaylist(@RequestParam("playListName")String playlistName) throws IOException, SpotifyWebApiException {
        this.playlistName = playlistName;
        createPlayList(playlistName);
        for (int i = 0; i < 10 ; i++) {
            List<String> recommendedSong = getReccommendations();
            addSongToPlaylist(String.valueOf(recommendedSong), playlistName);
        }
        return new RedirectView("/");
    }

    @ModelAttribute("playlistlink")
    public String playListSource(Model model) throws IOException, SpotifyWebApiException {
        String base = "https://open.spotify.com/embed/playlist/";
        String playlistID = getPlayListID(playlistName);
        String link = base + playlistID;
        model.addAttribute("playlistlink",link);
        return "playlistlink";
    }

    @GetMapping("/track")
    @ResponseBody
    public PlaylistTrack[] getTrackOfAPlaylist(String playlistName) throws IOException, SpotifyWebApiException {
        String playListID = getPlayListID("TestFromFrontEnd");

        PlaylistTrack[] playListTracks = spotifyApi.getPlaylistsTracks(playListID).build().execute().getItems();

        List<String> trackList = new ArrayList<>();

        for (PlaylistTrack track: playListTracks) {
            trackList.add( track.getTrack().getName());
        }
        return playListTracks;


    }

    @GetMapping("/asd")
    public String playlistLink(Model model){
    model.addAttribute("asd", "asddd");
    return "link";


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
        }
        return trackList;
    }

    @GetMapping("/addSongToPlaylist/{name}")
    private void addSongToPlaylist(@PathVariable("name")String songName, String playListName) throws IOException, SpotifyWebApiException {
        String playListId = getPlayListID(playListName);
        String[] songID = getTrackURI(songName);
        spotifyApi.addTracksToPlaylist(playListId, songID).build().execute();
    }

    @GetMapping("/getAnalysis")
    @ResponseBody
    public AudioAnalysis getAudioAnalysis() throws IOException, SpotifyWebApiException {
        System.out.println();
       AudioAnalysis analysis = spotifyApi.getAudioAnalysisForTrack("01iyCAUm8EvOFqVWYJ3dVX").build().execute();
        System.out.println(analysis.getTrack().getDuration());
        return analysis;
    }

    @GetMapping("/next")
    public RedirectView nextSong() throws IOException, SpotifyWebApiException {
        spotifyApi.skipUsersPlaybackToNextTrack().build().execute();
        return new RedirectView("http://localhost:8080");
    }

    @GetMapping("/start")
    private RedirectView startMusic() throws IOException, SpotifyWebApiException {
        spotifyApi.startResumeUsersPlayback().build().execute();
        System.out.println("Music started");
        return new RedirectView("http://localhost:8080");
    }

    @GetMapping("/pause")
    private RedirectView stopMusic() throws IOException, SpotifyWebApiException {
        spotifyApi.pauseUsersPlayback().build().execute();
        return new RedirectView("http://localhost:8080");
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
