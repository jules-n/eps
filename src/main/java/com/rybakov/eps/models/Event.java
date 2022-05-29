package com.rybakov.eps.models;

import java.util.Date;

public class Event {
    private int idEvent;
    private String eventName;
    private String place;
    private Date date;
    private String description;
    private int idStatus;
    private int idType;
    private int idOwner;

    public Event(){}

    public Event(String eventName, String place, Date date, String description, int idStatus, int idType, int idOwner) {
        this.eventName = eventName;
        this.place = place;
        this.date = date;
        this.description = description;
        this.idStatus = idStatus;
        this.idType = idType;
        this.idOwner = idOwner;
    }

    public Event(int idEvent, String eventName, String place, Date date, String description, int idStatus, int idType, int idOwner) {
        this.idEvent = idEvent;
        this.eventName = eventName;
        this.place = place;
        this.date = date;
        this.description = description;
        this.idStatus = idStatus;
        this.idType = idType;
        this.idOwner = idOwner;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String name) {
        this.eventName = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }
}
