package com.example.bagrutapp;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ReplayActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private TextView tvReplayStatus;
    private String[] moves;
    private int moveIndex = 0;
    private Handler handler = new Handler();
    private Runnable playMoveRunnable;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);

        tvReplayStatus = findViewById(R.id.tv_replay_status);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String btnId = "btn_" + i + j;
                int resId = getResources().getIdentifier(btnId, "id", getPackageName());
                buttons[i][j] = findViewById(resId);
                buttons[i][j].setEnabled(false); // לא מאפשר לחיצה במהלך ריפליי
            }
        }

        // ⬇ קבלת הנתונים מ־Intent
        String player1 = getIntent().getStringExtra("player1");
        String player2 = getIntent().getStringExtra("player2");
        String winner = getIntent().getStringExtra("winner");
        String movesString = getIntent().getStringExtra("moves");
        String durationString = getIntent().getStringExtra("duration");

        if (player1 == null || player2 == null || winner == null || movesString == null || durationString == null) {
            Toast.makeText(this, "שגיאה בנתוני המשחק", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ⬇ עיבוד הנתונים
        tvReplayStatus.setText(player1 + " vs " + player2 + " - Winner: " + winner);
        moves = movesString.split(",");
        try {
            String[] parts = durationString.split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            duration = minutes * 60 + seconds;
        } catch (Exception e) {
            duration = 0;
        }


        TextView tvDuration = findViewById(R.id.tv_duration);
        tvDuration.setText("Time: " + formatDuration(duration));

        startReplay();

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void startReplay() {
        playMoveRunnable = new Runnable() {
            @Override
            public void run() {
                if (moveIndex >= moves.length) return;

                String move = moves[moveIndex];
                char symbol = move.charAt(0);
                int i = Character.getNumericValue(move.charAt(1));
                int j = Character.getNumericValue(move.charAt(2));

                buttons[i][j].setText(String.valueOf(symbol));
                moveIndex++;

                handler.postDelayed(this, 800); // השהיה של 800ms בין מהלכים
            }
        };

        handler.postDelayed(playMoveRunnable, 1000); // התחלה אחרי שנייה
    }

    private String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(playMoveRunnable);
    }
}
