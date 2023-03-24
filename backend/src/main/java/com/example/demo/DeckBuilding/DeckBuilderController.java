package com.example.demo.DeckBuilding;


import Cards.Card;
import com.example.demo.DeckBuilding.Services.DeckBuilder;
import com.example.demo.DeckBuilding.Services.DeckBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Service
@RequestMapping(path = "/DeckBuilder")
public class DeckBuilderController {

    private DeckBuilder DeckBuilderService;

    @Autowired
    public DeckBuilderController() {
        DeckBuilderService = DeckBuilderFactory.GetDeckBuilderService();
    }

    @GetMapping(path ="GetAllCards")
    @CrossOrigin
    public List<Card> GetAllCards(){
        return DeckBuilderService.GetAllCards();
    }

}
