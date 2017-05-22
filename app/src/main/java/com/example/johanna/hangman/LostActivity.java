package com.example.johanna.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LostActivity extends AppCompatActivity {

    /**
     * Activity is shown when the player lost the game. On button click he has the chance to switch
     * to start activity again.
     **/


    private Button button;
    private TextView solution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);
        button = (Button) findViewById(R.id.againButton);
        solution = (TextView) findViewById(R.id.solutionText);
        solution.setText("The word was: " + getIntent().getExtras().get("word").toString());
        registerButtonListener();
    }


    public void registerButtonListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAgain();
            }
        });
    }

    public void tryAgain() {
        StartActivity startActivity = new StartActivity();
        Intent toStart = new Intent(LostActivity.this, StartActivity.class);
        startActivity(toStart);
    }
}
