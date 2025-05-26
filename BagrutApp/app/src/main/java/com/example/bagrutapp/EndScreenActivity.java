package com.example.bagrutapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class EndScreenActivity extends AppCompatActivity {

    private TextView tvWinner;
    private Button btnNewGame, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // הופך את האקטיביטי לפופאפ
        setContentView(R.layout.activity_end_screen);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(false);

        tvWinner = findViewById(R.id.tv_winner);
        btnNewGame = findViewById(R.id.btn_new_game);
        btnExit = findViewById(R.id.btn_exit);

        // מקבל את המידע מה-intent
        String winner = getIntent().getStringExtra("winner");
        String player1 = getIntent().getStringExtra("player1");
        String player2 = getIntent().getStringExtra("player2");
        boolean isAI = getIntent().getBooleanExtra("isAI", false);
        ArrayList<String> moves = getIntent().getStringArrayListExtra("moves");
        long duration = getIntent().getLongExtra("duration", 0); // קיבלנו מ-GameActivity
        String durationStr = getIntent().getStringExtra("duration");

        // מראה את שם המנצח
        tvWinner.setText("Winner: " + winner);

        // שמירת הלוג
        String logTitle = player1 + " vs " + player2 + " - Winner: " + winner + " - Time: " + formatDuration(duration);
        GameLogManager.saveGameLog(this, logTitle, moves, (int) duration);
        // יצירת אובייקט GameSummary
        GameSummary summary = new GameSummary(player1, player2, winner, durationStr, String.join(",", moves));

// שמירה ב-SQLite
        GameSummaryDatabaseHelper dbHelper = new GameSummaryDatabaseHelper(this);
        dbHelper.insertGameSummary(summary);

        // לחצן משחק חדש
        btnNewGame.setOnClickListener(v -> {
            Intent intent = new Intent(EndScreenActivity.this, GameActivity.class);
            intent.putExtra("player1", player1);
            intent.putExtra("player2", player2);
            intent.putExtra("isAI", isAI);
            startActivity(intent);
            finish();
        });

        // לחצן יציאה
        btnExit.setOnClickListener(v -> {
            Intent intent = new Intent(EndScreenActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private String formatDuration(long seconds) {
        int minutes = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        return String.format("%02d:%02d", minutes, secs);
    }
}
