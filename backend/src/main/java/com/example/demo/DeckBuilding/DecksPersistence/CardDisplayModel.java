package com.example.demo.DeckBuilding.DecksPersistence;


import javax.persistence.*;

@Entity
@Table(name = "cardsindeck")
class CardDisplayModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String cardname;
    private int cardpoints;

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
}
