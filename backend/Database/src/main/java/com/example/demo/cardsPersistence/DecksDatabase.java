package com.example.demo.cardsPersistence;

import com.example.demo.Cards.CardDisplay;
import com.example.demo.Consts;
import com.example.demo.DeckBuilding.DeckBuilder;
import org.hibernate.Session;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecksDatabase {

    public static void setProdDbPassword(String password) {
        Map<String, String> dbProperties_prod = new HashMap<>() {{
            put("javax.persistence.jdbc.password", password);
        }};
//        emf = Persistence.createEntityManagerFactory("production",dbProperties_prod);
        emf = Persistence.createEntityManagerFactory("development");
    }

    public static boolean registerUser(String username, String password) {
        Session s = emf.createEntityManager().unwrap(Session.class);
        boolean isUnique = false;
        try {
            User u = new User();
            u.setName(username);
            u.setPassword(password);

            s.getTransaction().begin();

            TypedQuery<User> tq = s.createQuery("SELECT u FROM User u WHERE name = :uname");
            tq.setParameter("uname", username);
            List<User> foundAdmins = tq.getResultList();
            isUnique = foundAdmins.isEmpty();

            if(isUnique) {
                s.persist(u);
            }

            s.getTransaction().commit();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return isUnique;
    }

    public static List<User> getUsers() {
        Session s = emf.createEntityManager().unwrap(Session.class);
        List<User> result = new ArrayList<>();
        try {

            s.getTransaction().begin();

            TypedQuery<User> tq = s.createQuery("SELECT u FROM User u");
            result = tq.getResultList();

            s.getTransaction().commit();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean registerAdmin(String username, String password) {
        Session s = emf.createEntityManager().unwrap(Session.class);
        boolean isUnique = false;
        try {
            Admin a = new Admin();
            a.setName(username);
            a.setPassword(password);

            s.getTransaction().begin();

            TypedQuery<Admin> tq = s.createQuery("SELECT a FROM Admin a WHERE name = :uname");
            tq.setParameter("uname", username);
            List<Admin> foundAdmins = tq.getResultList();
            isUnique = foundAdmins.isEmpty();

            if(isUnique) {
                s.persist(a);
            }

            s.getTransaction().commit();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return isUnique;
    }

    public static void deleteUser(String username) {
        Session s = emf.createEntityManager().unwrap(Session.class);
        try {
            s.getTransaction().begin();

            TypedQuery<User> tq = s.createQuery("SELECT u FROM User u WHERE name = :uname");
            tq.setParameter("uname", username);
            User u = tq.getSingleResult();

            s.remove(u);

            s.getTransaction().commit();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkAdminData(String username, String password) {
        Session s = emf.createEntityManager().unwrap(Session.class);
        boolean isPresent = false;
        try {
            s.getTransaction().begin();

            TypedQuery<Admin> tq = s.createQuery("SELECT a FROM Admin a WHERE name = :uname AND password = :pword");
            tq.setParameter("uname", username);
            tq.setParameter("pword", password);
            List<Admin> foundAdmins = tq.getResultList();
            isPresent = foundAdmins.size() > 0;

            s.getTransaction().commit();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return isPresent;
    }
    public static boolean checkUserData(String username, String password) {
         Session s = emf.createEntityManager().unwrap(Session.class);
         boolean isPresent = false;
        try {
            s.getTransaction().begin();

            TypedQuery<User> tq = s.createQuery("SELECT u FROM User u WHERE name = :uname AND password = :pword");
            tq.setParameter("uname", username);
            tq.setParameter("pword", password);
            List<User> foundUsers = tq.getResultList();
            isPresent = foundUsers.size() > 0;

            s.getTransaction().commit();

        } catch(Exception e) {
            e.printStackTrace();
        }
        return isPresent;
    }

    private static EntityManagerFactory emf;
    public static void createDeck(String username, String deckFraction,String deckname) {
        Session s = emf.createEntityManager().unwrap(Session.class);
        try {
            DeckModel deckToSave = new DeckModel();
            deckToSave.setUsername(username);
            deckToSave.setDeckname(deckname);
            deckToSave.setFraction(deckFraction);

            s.getTransaction().begin();

            s.persist(deckToSave);

            s.getTransaction().commit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        s.close();

    }
    public static void deleteDeck(String username, String deckname) {
        Session s = emf.createEntityManager().unwrap(Session.class);
        try {
            s.getTransaction().begin();


            TypedQuery<DeckModel> tq = s.createQuery("SELECT dm FROM DeckModel dm WHERE deckname = :dname AND username = :uname");
            tq.setParameter("dname", deckname);
            tq.setParameter("uname", username);
            DeckModel deckToDelete = tq.getSingleResult();
            deckToDelete.getCards().forEach(c -> s.remove(c));
            s.remove(deckToDelete);

            s.getTransaction().commit();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        s.close();

    }
    public static void saveDeck(String username, String deckname,String deckFraction, List<CardDisplay> cards) {
        Session s = emf.createEntityManager().unwrap(Session.class);
        deleteDeck(username, deckname);
        try {
            s.getTransaction().begin();

            DeckModel deckToSave = new DeckModel();
            deckToSave.setDeckname(deckname);
            deckToSave.setUsername(username);
            deckToSave.setFraction(deckFraction);

            List<CardDisplayModel> cardsToSave = new ArrayList<>();
            for (CardDisplay card : cards) {
                CardDisplayModel c = new CardDisplayModel();
                c.setCardname(card.getName());
                c.setCardpoints(card.getBasePoints());
                c.setDeck(deckToSave);
                c.setColor(card.getColor());
                c.setFraction(card.getFraction());
                cardsToSave.add(c);
            }

            s.persist(deckToSave);
            cardsToSave.forEach(c -> s.persist(c));

            s.getTransaction().commit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        s.close();

    }
    public static DeckBuilder load(String username) {
        Session s = emf.createEntityManager().unwrap(Session.class);

        s.getTransaction().begin();
        TypedQuery<DeckModel> tq = s.createQuery("SELECT dm FROM DeckModel dm WHERE username = :uname", DeckModel.class);
        tq.setParameter("uname", username);
        List<DeckModel> decks = tq.getResultList();


        DeckBuilder result = new DeckBuilder();
        for (DeckModel deck : decks) {
            result.createDeck(deck.getDeckname(), deck.getFraction());

            try{
                List<CardDisplay> cards = cardDisplayModel_to_cardDisplay(deck.getCards());
                for (CardDisplay card : cards) {
                    result.addCardToDeck(card, deck.getDeckname());
                }
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
        s.getTransaction().commit();
        s.close();
        return result;
    }

    private static List<CardDisplay> cardDisplayModel_to_cardDisplay(List<CardDisplayModel> cards) {
        List<CardDisplay> result = new ArrayList<>();

        for (int i = 0; i < cards.size(); i++) {
            CardDisplayModel card = cards.get(i);
            result.add(new CardDisplay(card.getCardname(), card.getCardpoints(), card.getColor(), card.getFraction()) );
        }
        return result;
    }

}
