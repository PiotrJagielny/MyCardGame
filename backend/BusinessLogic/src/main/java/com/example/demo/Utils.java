package com.example.demo;

import com.example.demo.CardsServices.CardDisplay;

import java.util.Random;

public class Utils {
    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min +1) + min;
    }

    

}
