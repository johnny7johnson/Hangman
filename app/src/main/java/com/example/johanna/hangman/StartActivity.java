package com.example.johanna.hangman;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private EditText mInput;
    private Button mOkButton;
    private Integer mWordLength;
    private CheckBox offlineMode;
    private Button diffButton;
    private String difficulty;
    private CheckBox sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mInput = (EditText) findViewById(R.id.wordlengthInput);
        mOkButton = (Button) findViewById(R.id.okButton);
        offlineMode = (CheckBox) findViewById(R.id.checkBox);
        diffButton = (Button) findViewById(R.id.difficultyButton);
        sound = (CheckBox) findViewById(R.id.sound);
        diffButton.setText(" Difficulty: medium ");
        difficulty = "medium";
        registerDiffButtonListener();
        registerButtonListener();

    }


    /**
     * decides what happens when player clicks on button
     */
    private void registerButtonListener() {
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInput.getText().length() != 0) {
                    mWordLength = Integer.parseInt(mInput.getText().toString());
                } else {
                    mWordLength = 1;
                }

                if (offlineMode.isChecked()) {
                    openOfflineDialog();
                } else {
                    startGame();
                }
            }
        });
    }


    private void registerDiffButtonListener() {
        diffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDifficultyDialog();
            }
        });
    }


    private void openDifficultyDialog() {
        final NumberPicker picker = new NumberPicker(StartActivity.this);
        picker.setMinValue(0);
        picker.setMaxValue(2);
        picker.setDisplayedValues(new String[]{"simple", "medium", "difficult"});
        picker.computeScroll();
        picker.setVisibility(View.VISIBLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Difficulty");
        builder.setView(picker);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int value = picker.getValue();
                if (value == 0) {
                    diffButton.setText(" Difficulty: simple ");
                    difficulty = "simple";
                } else if (value == 1) {
                    diffButton.setText(" Difficulty: medium ");
                    difficulty = "medium";
                } else if (value == 2) {
                    diffButton.setText(" Difficulty: hard ");
                    difficulty = "difficult";
                }

            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * alert dialog for initializing offline game
     */
    private void openOfflineDialog() {
        final EditText input = new EditText(this);
        input.setHint("type your own word");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Type in your own word and give the phone to your neighbour. He has to guess.")
                .setTitle("Offline Mode:");
        builder.setView(input);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String inputWord = input.getText().toString();
                if (inputWord.length() != 0) {
                    Integer wordLength = inputWord.length();
                    startGame(inputWord, wordLength);
                } else {
                    Toast.makeText(StartActivity.this, "Please give me a word", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    /**
     * switch to hangman activity for online game
     **/
    public void startGame() {
        HangmanActivity hangmanActivity = new HangmanActivity();
        Intent toHangman = new Intent(StartActivity.this, HangmanActivity.class);
        toHangman.putExtra("offlineMode", offlineMode.isChecked());
        toHangman.putExtra("wordlength", mWordLength);
        toHangman.putExtra("difficulty", difficulty);
        toHangman.putExtra("sound", sound.isChecked());
        startActivity(toHangman);
    }

    /**
     * switch to hangman activity for offline game
     *
     * @param word   word wich will be guessed
     * @param length length of word
     **/
    public void startGame(String word, Integer length) {
        HangmanActivity hangmanActivity = new HangmanActivity();
        Intent toHangman = new Intent(StartActivity.this, HangmanActivity.class);
        toHangman.putExtra("offlineMode", offlineMode.isChecked());
        toHangman.putExtra("word", word);
        toHangman.putExtra("length", length);
        toHangman.putExtra("difficulty", difficulty);
        toHangman.putExtra("sound", sound.isChecked());
        startActivity(toHangman);
    }


}
