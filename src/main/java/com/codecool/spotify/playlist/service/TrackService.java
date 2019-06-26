package com.codecool.spotify.playlist.service;

import com.codecool.spotify.playlist.controller.SpotifyApiController;
import com.codecool.spotify.playlist.model.Song;
import com.codecool.spotify.playlist.model.Track;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

@Service
public class TrackService {

    @Autowired
    RestTemplate restTemplate;

//    public ResponseEntity<Song> getRelevantDataFromTrack(Paging<Track> song){
//        Track[] songItems = song.getItems();
//
//    }

//    public JsonObject getRelevantDataFromTrack(Paging<Track> song){
//        JsonObject object = (JsonObject) new JsonParser().parse(Arrays.toString(song.getItems()));
//        JsonObject track = (JsonObject) object.get("track");
//        JsonObject artist = (JsonObject) track.get("artist");
//        JsonObject name = (JsonObject) artist.get("name");
//        return name;
//    }

    public Track getRelevanData() throws IOException, SpotifyWebApiException {
        return null;
    }
}
