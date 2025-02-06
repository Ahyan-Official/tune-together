package com.app.tunetogether.az;

public class EventDB {

    String location,name_venue,latitude,longitude,country,starts_at,documentId, image,artist_name;

    public EventDB() {
    }

    public EventDB(String location, String name_venue, String latitude, String longitude, String country, String starts_at, String documentId, String image_url, String artist_name) {
        this.location = location;
        this.name_venue = name_venue;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.starts_at = starts_at;
        this.documentId = documentId;
        this.image = image_url;
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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }
}
