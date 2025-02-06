package com.app.tunetogether.az;

public class Event {

    String location,name_venue,latitude,longitude,country,starts_at,id,image_url,artist_name;

    public Event() {
    }

    public Event(String location, String name_venue, String latitude, String longitude, String country, String starts_at, String id, String image_url,String artist_name) {
        this.location = location;
        this.name_venue = name_venue;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.starts_at = starts_at;
        this.id = id;
        this.image_url = image_url;
        this.artist_name = artist_name;

    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName_venue() {
        return name_venue;
    }

    public void setName_venue(String name_venue) {
        this.name_venue = name_venue;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStarts_at() {
        return starts_at;
    }

    public void setStarts_at(String starts_at) {
        this.starts_at = starts_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
