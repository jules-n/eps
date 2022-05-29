package com.rybakov.eps.models;

public class Invitation {
    private int idInvitation;
    private String text;
    private Event event;
    private Participant organizer;
    private Boolean isAccepted;

    public Invitation(int idInvitation, String text, Event event, Participant organizer, Boolean isAccepted) {
        this.idInvitation = idInvitation;
        this.text = text;
        this.event = event;
        this.organizer = organizer;
        this.isAccepted = isAccepted;
    }

    public int getIdInvitation() {
        return idInvitation;
    }

    public void setIdInvitation(int idInvitation) {
        this.idInvitation = idInvitation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Participant getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Participant organizer) {
        this.organizer = organizer;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }
}
