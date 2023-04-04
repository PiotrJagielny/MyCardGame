package com.example.demo.CardsParserTests;

import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Parser.CardsParser;
import com.example.demo.CardsServices.Parser.NormalCardsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestCardsParser {

    CardsParser parser;

    @BeforeEach
    public void setUp(){
        parser = new NormalCardsParser();
    }

    @Test
    public void createdParser_isEmpty(){
        assertTrue(parser.getParsedCards().isEmpty());
    }

    @Test
    public void addedDisplaysToParser_isNotEmpty(){
        parser.addCardsToParse(List.of("Pierwsza", "Druga"));

        assertFalse(parser.getParsedCards().isEmpty());
    }

    @Test
    public void testCardsParsing(){
        List<String> cases = List.of("Knight", "Viking");
        parser.addCardsToParse(cases);
        List<Card> cards = parser.getParsedCards();

        assertEquals(cards.size(), cards.size(), "cards and displays added to parser are not equal");

        for(int i = 0 ; i < cases.size() ; ++i){
            assertEquals(cases.get(i), cards.get(i).getDisplay());
        }
    }


}