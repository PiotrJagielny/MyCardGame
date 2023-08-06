package com.example.demo.DeckBuilding.DecksPersistence;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.DeckBuilding.DeckBuilder;
import org.hibernate.Session;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DecksDatabase {

    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("development");
//    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("production");
    public static void saveDeck(String username, String deckname, List<CardDisplay> cardsInDeck) {
        Session em = emf.createEntityManager().unwrap(Session.class);
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
        Session em = emf.createEntityManager().unwrap(Session.class);

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
        Session s = emf.createEntityManager().unwrap(Session.class);

        TypedQuery<DeckModel> tq = s.createQuery("SELECT dm FROM DeckModel dm WHERE username = :uname", DeckModel.class);
        tq.setParameter("uname", username);
        List<DeckModel> decks = tq.getResultList();


        DeckBuilder result = new DeckBuilder();
        for (DeckModel deck : decks) {
            result.createDeck(deck.getName());
            result.selectDeck(deck.getName());

            List<CardDisplay> cards = cardDisplayModel_to_cardDisplay(deck.getCards());
            for (CardDisplay card : cards) {
                result.addCardToDeck(card);
            }
        }

        return result;
    }

    private static List<CardDisplay> cardDisplayModel_to_cardDisplay(List<CardDisplayModel> cards) {
        List<CardDisplay> result = new ArrayList<>();

        for (int i = 0; i < cards.size(); i++) {
            CardDisplayModel card = cards.get(i);
            result.add(new CardDisplay(card.getCardname(), card.getCardpoints()));
        }
        return result;
    }
}
