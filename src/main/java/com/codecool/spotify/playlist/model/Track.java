package com.codecool.spotify.playlist.model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "artists",
        "availableMarkets",
        "discNumber",
        "durationMs",
        "externalUrls",
        "href",
        "id",
        "isPlayable",
        "linkedFrom",
        "name",
        "previewUrl",
        "trackNumber",
        "type",
        "uri",
        "isExplicit"
})
@Data
public class Track {

    @JsonProperty("artists")
    public List<Artist> artists = null;
    @JsonProperty("availableMarkets")
    public List<String> availableMarkets = null;
    @JsonProperty("discNumber")
    public Integer discNumber;
    @JsonProperty("durationMs")
    public Integer durationMs;
    @JsonProperty("externalUrls")
    public ExternalUrls__ externalUrls;
    @JsonProperty("href")
    public String href;
    @JsonProperty("id")
    public String id;
    @JsonProperty("isPlayable")
    public Object isPlayable;
    @JsonProperty("linkedFrom")
    public Object linkedFrom;
    @JsonProperty("name")
    public String name;
    @JsonProperty("previewUrl")
    public String previewUrl;
    @JsonProperty("trackNumber")
    public Integer trackNumber;
    @JsonProperty("type")
    public String type;
    @JsonProperty("uri")
    public String uri;
    @JsonProperty("isExplicit")
    public Boolean isExplicit;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}