package com.example.bagrutapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class SavedGamesActivity extends AppCompatActivity {

    private ListView listViewGames;
    private List<GameSummary> gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);

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
            Intent intent = new Intent(SavedGamesActivity.this, ReplayActivity.class);
            intent.putExtra("player1", selected.getPlayer1());
            intent.putExtra("player2", selected.getPlayer2());
            intent.putExtra("winner", selected.getWinner());
            intent.putExtra("duration", selected.getDuration());
            intent.putExtra("moves", selected.getMoves()); // נשלח כמחרוזת מופרדת בפסיקים
            startActivity(intent);
        });
    }
}
