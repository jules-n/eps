package com.rybakov.eps.dao;

import com.rybakov.eps.models.Event;
import com.rybakov.eps.models.Invitation;
import com.rybakov.eps.models.Participant;

import java.util.List;

public interface IDAO<T, K> {
//я честно хотел сделать всё через рефлексию и пару-тройку методов, но эта залупа то и дело бросала хуй пойми на что исключения
    Participant readParticipant(String login, String password);
    List readAll(T entity);
    boolean create(T entity);
    List readAllBy(T entity, K key);
    boolean update(T entity, K key);
    boolean archiveEvents(K key);
    boolean updateAllEventData(Event event, K key);
    T readByKey(T entity, K key);
    boolean create(int[] participants, int event, String text, int owner);
    List<Invitation> readMyInvitation(K key);
    boolean confirm(int user, K key, Boolean decision);

}
