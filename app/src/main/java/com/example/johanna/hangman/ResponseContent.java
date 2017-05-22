package com.example.johanna.hangman;

/**
 * Created by Johanna on 19.12.2016.
 */

public class ResponseContent {
    private int wordLength;
    private String word;

    public ResponseContent(int length, String word) {
        this.wordLength = length;
        this.word = word;
    }

    public int getLength() {
        return wordLength;
    }

    public String getWord() {
        return word;
    }

}
