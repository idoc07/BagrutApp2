package com.example.bagrutapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class GameLogsActivity extends BaseActivity {

    private ListView listViewGames;
    private List<GameSummary> gameList;
    private Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_logs);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewGames = findViewById(R.id.listViewGames);

        GameSummaryDatabaseHelper dbHelper = new GameSummaryDatabaseHelper(this);
        gameList = dbHelper.getAllGameSummaries();

        List<String> titles = new ArrayList<>();
        for (GameSummary g : gameList) {
            titles.add(g.getPlayer1() + " vs " + g.getPlayer2() + " - " + g.getWinner() + " - " + g.getDuration());
        }

        GameSummaryAdapter adapter = new GameSummaryAdapter(this, gameList);
        listViewGames.setAdapter(adapter);

        listViewGames.setOnItemClickListener((parent, view, position, id) -> {
            GameSummary selected = gameList.get(position);
            Intent intent = new Intent(GameLogsActivity.this, ReplayActivity.class);
            intent.putExtra("player1", selected.getPlayer1());
            intent.putExtra("player2", selected.getPlayer2());
            intent.putExtra("winner", selected.getWinner());
            intent.putExtra("duration", selected.getDuration());
            intent.putExtra("moves", selected.getMoves()); // נשלח כמחרוזת מופרדת בפסיקים
            startActivity(intent);
        });

        Button btnBackToMain = findViewById(R.id.btn_back_to_main);
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(GameLogsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

    }
}
