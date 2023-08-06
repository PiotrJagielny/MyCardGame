package com.example.demo.DeckBuilding.DecksPersistence;

import com.example.demo.CardsServices.CardDisplay;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "decks")
class DeckModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String username;


    @OneToMany(mappedBy = "deck")
    private List<CardDisplayModel> cards;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CardDisplayModel> getCards() {
        return cards;
    }

    public void setCards(List<CardDisplayModel> cards) {
        this.cards = cards;
    }
}
