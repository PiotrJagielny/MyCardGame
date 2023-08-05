package com.example.demo.DeckBuilding.DecksPersistence;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.DeckBuilding.DeckBuilder;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecksDatabase {







    
    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("development");
//    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("production");



    public static void saveDeck(String username, String deckname, List<CardDisplay> cardsInDeck) {
        EntityManager em = emf.createEntityManager();
        deleteDeck(username, deckname);

        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();

            List<DeckModel> dataToSave = new ArrayList<>();
            cardsInDeck.forEach(c -> dataToSave.add(new DeckModel(username, deckname, c.getName(), c.getPoints())));
            dataToSave.forEach(dm -> em.persist(dm));

            et.commit();
        }
        catch(Exception e) {
            if(et != null) {
                et.rollback();
            }
            e.printStackTrace();
        }
        em.close();
    }
    public static void deleteDeck(String username, String deckname) {
        EntityManager em = emf.createEntityManager();
        String query = "SELECT dm FROM DeckModel dm WHERE userName = :uName AND deckName = :dName";
        TypedQuery<DeckModel> tq = em.createQuery(query, DeckModel.class);
        tq.setParameter("uName", username);
        tq.setParameter("dName", deckname);
        List<DeckModel> fetchedDeck = tq.getResultList();

        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();

            fetchedDeck.forEach(dm -> em.remove(dm));

            et.commit();
        }
        catch(Exception e) {
            if(et != null) {
                et.rollback();
            }
            e.printStackTrace();
        }
        em.close();
    }
    public static DeckBuilder load(String username) {
        EntityManager em = emf.createEntityManager();
        String getAllUserDecksQuery = "SELECT dm FROM DeckModel dm WHERE dm.userName = :userName";
        TypedQuery<DeckModel> tq = em.createQuery(getAllUserDecksQuery, DeckModel.class);
        tq.setParameter("userName", username);
        List<DeckModel> decks;

        DeckBuilder result = new DeckBuilder();
        try {
            decks = tq.getResultList();
            List<String> decksNames = decks.stream().map(DeckModel::getDeckName).distinct().collect(Collectors.toList());
            for (DeckModel dm : decks) {
                result.createDeck(dm.getDeckName());
                result.selectDeck(dm.getDeckName());
                result.addCardToDeck(new CardDisplay(dm.getCardName(), dm.getCardPoints()));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        em.close();
        return result;
    }
}
