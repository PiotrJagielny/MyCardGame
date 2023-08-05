package com.example.demo.DeckBuilding.DecksPersistence;

import com.example.demo.CardsServices.CardDisplay;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cardsindeck")
public class DeckModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String deckName;
    private String cardName;
    private int cardPoints;

    public DeckModel(String userName, String deckName, String cardName, int cardPoints) {
        this.userName = userName;
        this.deckName = deckName;
        this.cardName = cardName;
        this.cardPoints = cardPoints;
    }

    public DeckModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getCardPoints() {
        return cardPoints;
    }

    public void setCardPoints(int cardPoints) {
        this.cardPoints = cardPoints;
    }

}
