package com.example.demo.DeckBuilding;


import Cards.Card;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/DeckBuilder")
public class DeckBuilderController {


    @GetMapping(path ="GetAllCards")
    @CrossOrigin
    public List<Card> GetAllCards(){
        List<Card> Cards = List.of(
                new Card("Knight"), new Card("Witch"), new Card("Thunder"), new Card("Warrior"),
                new Card("Viking"), new Card("Capitan"), new Card("Armageddon")
        );
        return Cards;
    }

}
