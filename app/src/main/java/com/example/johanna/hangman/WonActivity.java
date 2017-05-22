package com.example.johanna.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WonActivity extends AppCompatActivity {

    private Button againButton;
    private TextView solution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);
        againButton = (Button) findViewById(R.id.againButton);
        solution = (TextView) findViewById(R.id.solutionText);
        solution.setText("Your are good, the word is: " + getIntent().getExtras().get("word").toString());
        registerButtonListener();
    }

    public void registerButtonListener() {
        againButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAgain();
            }
        });
    }


    public void tryAgain() {
        StartActivity startActivity = new StartActivity();
        Intent toStart = new Intent(WonActivity.this, StartActivity.class);
        startActivity(toStart);
    }
}
