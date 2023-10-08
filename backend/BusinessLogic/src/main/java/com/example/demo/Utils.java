package com.example.demo;

import java.util.Random;

public final class Utils {

    private Utils() {}

    public static int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min +1) + min;
    }

    

}
