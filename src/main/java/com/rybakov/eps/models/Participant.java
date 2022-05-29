package com.rybakov.eps.models;

public class Participant{

    private int idParticipant;
    private String name;
    private String surname;
    private boolean isOrganizer;
    private long phone;
    private String login;
    private String password;

    public Participant(String name, String surname, boolean isOrganizer, long phone, String login, String password) {
        this.name = name;
        this.surname = surname;
        this.isOrganizer = isOrganizer;
        this.phone = phone;
        this.login = login;
        this.password = password;
    }

    public Participant(int idParticipant, String name, String surname, boolean isOrganizer, long phone, String login, String password) {
        this.idParticipant = idParticipant;
        this.name = name;
        this.surname = surname;
        this.isOrganizer = isOrganizer;
        this.phone = phone;
        this.login = login;
        this.password = password;
    }

    public Participant(){

    }

    public int getIdParticipant() {
        return idParticipant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isOrganizer() {
        return isOrganizer;
    }

    public void setOrganizer(boolean organizer) {
        isOrganizer = organizer;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
