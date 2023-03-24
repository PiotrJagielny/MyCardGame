package com.example.demo.DeckBuilding.Services;

public class DeckBuilderFactory {
    public static DeckBuilder GetDeckBuilderService(){
        return new DeckBuilderService();
    }
}
