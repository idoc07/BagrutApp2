package com.example.bagrutapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class GameLogsActivity extends AppCompatActivity {

    private ListView listViewGames;
    private Button btnBack;
    private List<GameLogEntry> gameLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_logs);

        listViewGames = findViewById(R.id.listViewGames);
        btnBack = findViewById(R.id.btnBack);

        gameLogs = GameLogManager.loadGameLogObjects(this);

        List<String> titles = new ArrayList<>();
        for (GameLogEntry log : gameLogs) {
            titles.add(log.getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        listViewGames.setAdapter(adapter);

        listViewGames.setOnItemClickListener((parent, view, position, id) -> {
            GameLogEntry gameLog = gameLogs.get(position);
            Intent intent = new Intent(GameLogsActivity.this, ReplayActivity.class);
            intent.putExtra("log_title", gameLog.getTitle());
            intent.putStringArrayListExtra("log_moves", new ArrayList<>(gameLog.getMoves()));
            intent.putExtra("duration", gameLog.getDuration());
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
