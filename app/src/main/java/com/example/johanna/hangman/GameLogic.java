package com.example.johanna.hangman;

import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Johanna on 09.01.2017.
 */


//Ideas: shadow, maxWordLength, (last high scores), (hangman (m/w)), schwierigkeitstufe: benutzte buchtaben (nicht) anzeigen


public class GameLogic {
    private String mWord;
    private Character mLetter;
    private ArrayList checkedLetters;
    private HangmanActivity hangmanActivity;

    public GameLogic(HangmanActivity hangmanActivity, String word) {
        this.hangmanActivity = hangmanActivity;
        this.mWord = word;
        checkedLetters = new ArrayList();
    }


    /**
     * checks if word contains the chosen letter
     *
     * @param checkletter chosen letter
     * @return true if letter is in word and is chosen the first time
     **/
    public boolean checkLetter(Character checkletter) {
        mLetter = checkletter;
        mLetter.toUpperCase(mLetter);
        boolean isNew;
        boolean isInWord;

        //checks if letter is in word
        if (getCharPosition(checkletter).size() == 0) {
            isInWord = false;
        } else {
            isInWord = true;
        }


        //checks if letter was chosen before
        if (checkedLetters.contains(mLetter)) {
            isNew = false;
        } else {
            checkedLetters.add(mLetter);
            isNew = true;
        }


        if (isNew && isInWord) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @param checkletter chosen letter
     * @returns Arraylist<Integer> with positions posititons of letter in the word
     */
    public ArrayList getCharPosition(Character checkletter) {
        mLetter = Character.toUpperCase(checkletter);
        String letter = mLetter.toString().toUpperCase();
        ArrayList<Integer> returnList = new ArrayList();
        if (mWord.contains(letter)) {
            char[] myCharArray = mWord.toCharArray();
            for (int i = 0; i < mWord.length(); i++) {
                if (myCharArray[i] == mLetter) {
                    returnList.add(i);
                }
            }
        }
        return returnList;
    }


    /**
     * checks if player has won the game
     **/
    public boolean checkWin() {

        ArrayList<TextView> textViews;
        textViews = hangmanActivity.getHangmanView().getTextViewList();
        for (int i = 0; i < textViews.size(); i++) {
            if (textViews.get(i).getText().equals("__")) {
                return false;
            }
        }
        return true;
    }

    public ArrayList getCheckedLetters() {
        return checkedLetters;
    }

}
