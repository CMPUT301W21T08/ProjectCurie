package com.example.projectcurie;

import java.util.Random;

/**
 * Class for generating unique random user names.
 * @author Joshua Billson
 */
public class NameGenerator {

    private static final String[] nouns = {"Wizard", "Rice", "Aubergine", "Tortilla", "Sundae",
            "Potato", "Cheese", "Badger", "Omelette", "Umbrella", "Licorice", "Ruby", "Cantaloupe",
            "Soup", "Crayon", "Tomato", "Banana", "Giraffe", "Snake", "Wasabi", "Oatmeal", "Yam",
            "Pancake", "Sushi", "Wren", "Bear", "Fox"};

    private static final String[] adjectives = {"Runny", "Soggy", "Iced", "Cold", "Ingenious",
            "Clever", "Observant", "Confused", "Excited", "Melodic", "Diligent", "Curious",
            "Lucky", "Dramatic", "Sassy", "Tangy", "Tasty", "Clumsy", "Inquisitive", "Quixotic",
            "Magical", "Exuberant", "Witty", "Vivacious", "Dazzling", "Prickly", "Slippery",
            "Strange", "Wise"};

    /**
     * Generate a unique random username.
     * @return A username.
     */
    public static String uniqueName() {
        Random rand = new Random();
        String adjective = adjectives[rand.nextInt(adjectives.length)];
        String noun = nouns[rand.nextInt(nouns.length)];
        String number = Integer.toString(rand.nextInt(100));
        return adjective + noun + number;
    }
}
