package com.example.demo.DeckBuilding;

import com.example.demo.CardsServices.CardDisplay;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DecksDatabase {
    private final static String jdbcURL = "jdbc:postgresql://localhost:5433/cardgame";
    private final static String dbusername = "postgres";
    private final static String password= "1234";
    public static void save(DeckBuilder deckBuilder, String username) {
        try {
            Connection connection = DriverManager.getConnection(jdbcURL,dbusername,password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM cardsindeck;");
            statement.executeUpdate("DELETE FROM decks;");

            List<String> decks = deckBuilder.getDecksNames();
            for (String deck : decks) {
                statement.executeUpdate("INSERT INTO decks(username,deckname) VALUES('" + username + "' , '" + deck + "');");

                deckBuilder.selectDeck(deck);
                List<CardDisplay> deckCards = deckBuilder.getCurrentDeck();
                for (CardDisplay card : deckCards) {
                    statement.executeUpdate(
                            "INSERT INTO cardsindeck(username, deckname, cardname, cardpoints) VALUES('" +username+ "','" + deck + "' , '" + card.getName()+ "' , '" + card.getPoints()+ "');"
                    );

                }
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static DeckBuilder load(String username) {
        DeckBuilder loadedDeckBuilder = new DeckBuilder();

        try {
            Connection connection = DriverManager.getConnection(jdbcURL,dbusername,password);
            Statement statement = connection.createStatement();
            ResultSet decks = statement.executeQuery("SELECT * FROM decks WHERE username = '" +username+ "';");
            List<String> decksNames = new ArrayList<>();
            while(decks.next()) {
                decksNames.add(decks.getString("deckname"));
            }

            for (String deckName : decksNames) {
                loadedDeckBuilder.createDeck(deckName);
                loadedDeckBuilder.selectDeck(deckName);

                ResultSet cardsInDeck = statement.executeQuery("SELECT * FROM cardsindeck WHERE username='"+username+"' AND deckname='"+deckName+"';");
                while(cardsInDeck.next()) {
                    String cardName = cardsInDeck.getString("cardname");
                    int cardPoints= cardsInDeck.getInt("cardpoints");
                    loadedDeckBuilder.addCardToDeck(new CardDisplay(cardName, cardPoints));
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return loadedDeckBuilder;
    }
}
