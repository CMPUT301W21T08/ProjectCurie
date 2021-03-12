package com.example.projectcurie;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

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
        String name = adjective + noun + number;
        if (! isUnique(name)) {
            return uniqueName();
        }
        return name;
    }

    /* Check That A Username Is Unique */
    private static boolean isUnique(String name) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AtomicBoolean exists = new AtomicBoolean(false);

        db.collection("users").document(name)
                .get()
                .addOnSuccessListener(document -> exists.set(document.exists()))
                .addOnFailureListener(e -> Log.i("Info", e.toString()));

        return ! exists.get();
    }
}
