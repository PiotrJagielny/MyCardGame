package com.example.demo.CardsParserTests;

import com.example.demo.CardsServices.Parser.CardsParser;
import com.example.demo.CardsServices.Parser.NormalCardsParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestCardsParser {

    @Test
    public void nothing(){
        CardsParser parser = new NormalCardsParser();

        assertEquals(null, parser.getParsedCards());
    }


}