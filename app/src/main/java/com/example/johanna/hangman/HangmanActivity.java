package com.example.johanna.hangman;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HangmanActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://swlab-ws1617.iap.hs-heilbronn.de/api/v0.1/";

    private String mWord;
    private int mLength;
    private Gson gson;
    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private GameLogic logic;
    private HangmanView myView;
    private int failNr = 0;
    private Character mLetter;
    private boolean offlineMode;
    private String difficulty;
    private int trial = 0;
    private boolean soundEnabled;
    private int rightNr = 0;


    /**
     * constructor is splitted in two possible parts for online and offline mode
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);
        offlineMode = getIntent().getExtras().getBoolean("offlineMode");
        soundEnabled = getIntent().getExtras().getBoolean("sound");
        if (offlineMode) {
            constructorOffline();
        } else {
            constructorOnline();
        }
    }

    private void constructorOffline() {
        mWord = getIntent().getExtras().get("word").toString().toUpperCase();
        mLength = getIntent().getExtras().getInt("length");
        logic = new GameLogic(this, mWord);
        restOfConstructor();
    }

    private void constructorOnline() {
        gson = new GsonBuilder().setDateFormat("'username','ldap'").create();           //evtl?
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))                 //evtl?
                .build();
        mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);
        getWord();
        ;
    }

    private void restOfConstructor() {
        myView = new HangmanView(HangmanActivity.this);
        setContentView(myView);
        difficulty = getIntent().getExtras().getString("difficulty");
        decideDifficulty();
    }

    private void decideDifficulty() {
        switch (difficulty) {
            case "simple":
                Character firstLetter = mWord.toCharArray()[0];
                handleNewLetter(firstLetter);
                break;
            case "medium":
                //standard implementation
                break;
            case "difficult":
                failNr = 5;
                break;
        }
    }

    public void getWord() {
        final Call<ResponseContent> getWord = mRetrofitAPI.getHangmanWord(getIntent().getExtras().get("wordlength").toString());
        getWord.enqueue(new Callback<ResponseContent>() {
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    mLength = response.body().getLength();
                    mWord = response.body().getWord().toUpperCase();
                    logic = new GameLogic(HangmanActivity.this, mWord);
//                    Toast.makeText(HangmanActivity.this, "Length: " + mLength + "\nWord: " + mWord, Toast.LENGTH_SHORT).show();

                } else {
                    mWord = "OFFLINE";
                    mLength = 7;
                    logic = new GameLogic(HangmanActivity.this, mWord);
                    Toast.makeText(HangmanActivity.this, "Sorry, something went wrong with your request", Toast.LENGTH_SHORT).show();
                }
                restOfConstructor();
            }

            @Override
            public void onFailure(Call<ResponseContent> call, Throwable t) {
                Toast.makeText(HangmanActivity.this, t.getMessage() + "\n Make sure your are VPN connected", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * method checks what event should be done after try out a new letter
     *
     * @param c letter chosen in alert dialog
     **/
    public void handleNewLetter(Character c) {

        int i;
        if (logic.checkLetter(c) == false) {
            //draw next part of hangman
            myView.invalidate();
            failNr++;
            if (soundEnabled) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wrong_answer);
                mediaPlayer.start();
            }
        } else {
            //draw letter in View
            myView.postInvalidate();
            mLetter = c.toUpperCase(c);
            rightNr++;
            if (soundEnabled) {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.coin_sound_effect);
                mediaPlayer.start();
            }

        }
    }

    public void checkWin() {
        //check if player won game
        boolean won = logic.checkWin();
        if (won == true && trial > 0) {
            playerWon();
        }
        trial++;
    }


    /**
     * switch to winActivity if player won game
     */
    public void playerWon() {
        if (soundEnabled) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.win_stage_sound);
            mediaPlayer.start();
        }
        WonActivity wonActivity = new WonActivity();
        Intent toWon = new Intent(HangmanActivity.this, WonActivity.class);
        toWon.putExtra("word", mWord);
        startActivity(toWon);
    }

    /**
     * switch to lostActivity if player lost game
     */
    public void playerLost() {
        if (soundEnabled) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.lost_a_life_sound);
            mediaPlayer.start();
        }
        LostActivity lostActivity = new LostActivity();
        Intent toLost = new Intent(HangmanActivity.this, LostActivity.class);
        toLost.putExtra("word", mWord);
        startActivity(toLost);
    }

    public int getLength() {
        return mLength;
    }

    public int getFailNr() {
        return failNr;
    }

    public ArrayList getPositions() {
        return logic.getCharPosition(mLetter);
    }

    public Character getLetter() {
        return mLetter;
    }

    public ArrayList<Character> getCheckedLetters() {
        return logic.getCheckedLetters();
    }

    public HangmanView getHangmanView() {
        return myView;
    }

    public int getRightNr() {
        return rightNr;
    }

    public String getDiff() {
        return difficulty;
    }

}
