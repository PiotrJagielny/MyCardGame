package com.example.demo.cardsPersistence;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cardsindeck")
class CardDisplayModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String cardname;
    private int cardpoints;
    private String color;
    private String fraction;

    @ManyToOne
    @JoinColumn(name="deckid", nullable = false)
    private DeckModel deck;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public int getCardpoints() {
        return cardpoints;
    }

    public void setCardpoints(int cardpoints) {
        this.cardpoints = cardpoints;
    }

    public DeckModel getDeck() {
        return deck;
    }

    public void setDeck(DeckModel deck) {
        this.deck = deck;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFraction() {
        return fraction;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }
}
