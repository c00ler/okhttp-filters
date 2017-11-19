package com.github.avenderov.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class Track {

    private final String name;

    private final String artist;

    @JsonCreator
    public Track(@JsonProperty("name") final String name, @JsonProperty("artist") final String artist) {
        this.name = name;
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Track track = (Track) o;
        return Objects.equals(name, track.name) &&
                Objects.equals(artist, track.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, artist);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Track{");
        sb.append("name='").append(name).append('\'');
        sb.append(", artist='").append(artist).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
