package com.example.demo.DeckBuilding;

import com.example.demo.CardsServices.CardDisplay;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DecksDatabase {
    private final static String jdbcURL = "jdbc:postgresql://localhost:5433/cardgame";
    private final static String dbusername = "postgres";
    private final static String password= "1234";
    public static void save(DeckBuilder deckBuilder, String username) {
        try {
            Connection connection = DriverManager.getConnection(jdbcURL,dbusername,password);
            Statement statement = connection.createStatement();
            String deleteDecks = "DELETE FROM decks;";
            String deleteDecksCards= "DELETE FROM cardsindeck;";
            statement.executeUpdate(deleteDecks);
            statement.executeUpdate(deleteDecksCards);

            List<String> decks = deckBuilder.getDecksNames();
            for (String deck : decks) {
                String insertDeckQuery = "INSERT INTO decks(username,deckname) VALUES('" + username + "' , '" + deck + "');";
                statement.executeUpdate(insertDeckQuery);

                deckBuilder.selectDeck(deck);
                List<CardDisplay> deckCards = deckBuilder.getCurrentDeck();
                for (CardDisplay card : deckCards) {
                    String insertCardQuery = "INSERT INTO cardsindeck(deckname, cardname, cardpoints) VALUES('" + deck + " ' , '" + card.getName()+ "' , '" + card.getPoints()+ "');";
                    statement.executeUpdate(insertCardQuery);

                }
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static DeckBuilder load(DeckBuilder deckBuilder, String username) {
        DeckBuilder loadedDecks = new DeckBuilder();

        try {
            Connection connection = DriverManager.getConnection(jdbcURL,dbusername,password);

            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return loadedDecks;
    }
}
