package com.example.demo.CardsParserTests;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.CardsServices.Cards.Card;
import com.example.demo.CardsServices.Cards.NormalCard;
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
        parser.addCardsToParse(List.of(new CardDisplay( "Pierwsza"),new CardDisplay( "Druga")));

        assertFalse(parser.getParsedCards().isEmpty());
    }

    @Test
    public void testCardsParsing(){
        List<CardDisplay> cases = List.of(new CardDisplay("Knight"),new CardDisplay( "Viking"));
        parser.addCardsToParse(cases);
        List<Card> cards = parser.getParsedCards();

        assertEquals(cards.size(), cards.size(), "cards and displays added to parser are not equal");

        for(int i = 0 ; i < cases.size() ; ++i){
            assertEquals(cases.get(i).getName(), cards.get(i).getDisplay().getName());
        }
    }

    @Test
    public void testCardsDisplayParse(){
        List<Card> cases = List.of(new NormalCard("Knight"));
        List<CardDisplay> displays = parser.getCardsDisplay(cases);

        assertEquals(cases.size(), displays.size());
        for(int i = 0 ; i < displays.size() ; ++i){
            assertEquals(cases.get(i).getDisplay().getName(), displays.get(i).getName());
        }
    }


}