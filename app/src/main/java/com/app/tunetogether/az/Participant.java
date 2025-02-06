package com.app.tunetogether.az;

public class Participant {

    String userId;


    public Participant() {
    }

    public Participant(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

