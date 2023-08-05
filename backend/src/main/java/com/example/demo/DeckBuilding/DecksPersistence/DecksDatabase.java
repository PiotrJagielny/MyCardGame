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
    private final static String jdbcURL = "jdbc:postgresql://localhost:5433/cardgame";
    private final static String dbusername = "postgres";
    private final static String password= "1234";
//    private final static String jdbcURL = "jdbc:postgresql://dpg-citnnqtiuiedpv4sj9hg-a.frankfurt-postgres.render.com/cardgame";
//    private final static String dbusername = "cardgame_user";
//    private final static String password= "Kaa338qGgKJbSlduEeUc880qA9PjTrWk";

    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("development");
    public static void save(DeckBuilder deckBuilder, String username) {
        EntityManager em = emf.createEntityManager();
        String getAllUserDecksQuery = "SELECT dm FROM DeckModel dm WHERE dm.userName = :userName";
        TypedQuery<DeckModel> tq = em.createQuery(getAllUserDecksQuery, DeckModel.class);
        List<DeckModel> decks;
    }
    public static void addCardToDeck(String username, String deckname, CardDisplay card) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = null;

        try {
            et = em.getTransaction();
            et.begin();

            DeckModel dm = new DeckModel();
            dm.setUserName(username);
            dm.setDeckName(deckname);
            dm.setCardName(card.getName());
            dm.setCardPoints(card.getPoints());
            em.persist(dm);

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
    public static void removeCardFromDeck(String username, String deckname, CardDisplay card) {
        EntityManager em = emf.createEntityManager();
        String getDeckModel = "SELECT dm FROM DeckModel dm WHERE userName = :uname AND deckName = :dname AND cardName = :cName AND cardPoints = :cPoints";
        TypedQuery<DeckModel> tq = em.createQuery(getDeckModel, DeckModel.class);
        tq.setParameter("")


    }
    public static void saveDeck(String username, String deckname, List<CardDisplay> cardsInDeck) {
        EntityManager em = emf.createEntityManager();
        String query = "SELECT dm FROM DeckModel dm WHERE userName = :uName AND deckName = :dName";
        TypedQuery<DeckModel> tq = em.createQuery(query, DeckModel.class);
        tq.setParameter("uName", username);
        tq.setParameter("dName", deckname);
        List<DeckModel> fetchedDeck = tq.getResultList();
        fetchedDeck.forEach(dm -> em.remove(dm));

        EntityTransaction et = null;
        try {
            et = em.getTransaction();
            et.begin();

            List<DeckModel> dataToSave = new ArrayList<>();
            cardsInDeck.forEach(c -> dataToSave.add(new DeckModel(username, deckname, c.getName(), c.getPoints())));

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
