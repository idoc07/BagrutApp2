package com.example.bagrutapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnTwoPlayers, btnAgainstAI, btnGameLogs, btnSavedGames;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTwoPlayers = findViewById(R.id.btn_two_players);
        btnAgainstAI = findViewById(R.id.btn_against_ai);
        btnGameLogs = findViewById(R.id.btn_game_logs);
        btnSavedGames = findViewById(R.id.btn_saved_games);

        btnTwoPlayers.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlayerSetupActivity.class);
            startActivity(intent);
        });

        btnAgainstAI.setOnClickListener(v -> showDifficultyDialog());

        btnGameLogs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameLogsActivity.class);
            startActivity(intent);
        });

        btnSavedGames.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SavedGamesActivity.class);
            startActivity(intent);
        });
    }

    private void showDifficultyDialog() {
        String[] difficulties = {"Easy", "Medium", "Hard"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Difficulty")
                .setItems(difficulties, (dialog, which) -> {
                    String difficulty = difficulties[which];
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra("isAI", true);
                    intent.putExtra("player1", "Player");
                    intent.putExtra("player2", "AI");
                    intent.putExtra("difficulty", difficulty);
                    startActivity(intent);
                });
        builder.create().show();
    }
}
