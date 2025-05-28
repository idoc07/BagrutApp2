package com.example.bagrutapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnTwoPlayers, btnAgainstAI, btnGameLogs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTwoPlayers = findViewById(R.id.btn_two_players);
        btnAgainstAI = findViewById(R.id.btn_against_ai);
        btnGameLogs = findViewById(R.id.btn_game_logs);

        btnTwoPlayers.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlayerSetupActivity.class);
            startActivity(intent);
        });

        btnAgainstAI.setOnClickListener(v -> showDifficultyDialog());

        btnGameLogs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameLogsActivity.class);
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

    private boolean isMusicPlaying = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle_music) {
            if (isMusicPlaying) {
                stopService(new Intent(this, MusicService.class));
                item.setIcon(R.drawable.baseline_volume_off_24);
            } else {
                startService(new Intent(this, MusicService.class));
                item.setIcon(R.drawable.baseline_volume_up_24);
            }
            isMusicPlaying = !isMusicPlaying;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
