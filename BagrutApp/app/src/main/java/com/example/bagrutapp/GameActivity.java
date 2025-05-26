package com.example.bagrutapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private String player1Name;
    private String player2Name;
    private boolean isAI;
    private String difficulty;
    private ArrayList<String> moves;
    private TextView tvTimer;
    private int elapsedSeconds = 0;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;

    private TextView textViewTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Button btnReset = findViewById(R.id.btn_reset);
        Button btnExit = findViewById(R.id.btn_exit);

        btnReset.setOnClickListener(v -> resetGame());
        btnExit.setOnClickListener(v -> finish());

        textViewTurn = findViewById(R.id.tv_turn);

        isAI = getIntent().getBooleanExtra("isAI", false);
        difficulty = getIntent().getStringExtra("difficulty");

        player1Name = getIntent().getStringExtra("player1");
        if (player1Name == null || player1Name.trim().isEmpty()) {
            player1Name = isAI ? "Player" : "Player 1";
        }

        player2Name = getIntent().getStringExtra("player2");
        if (player2Name == null || player2Name.trim().isEmpty()) {
            player2Name = isAI ? "AI" : "Player 2";
        }

        moves = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "btn_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        tvTimer = findViewById(R.id.tv_timer);

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                int minutes = elapsedSeconds / 60;
                int seconds = elapsedSeconds % 60;
                String time = String.format("%02d:%02d", minutes, seconds);
                tvTimer.setText(time);
                elapsedSeconds++;
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 1000);

        updateTurnText();
    }

    @Override
    public void onClick(View v) {
        Button clickedButton = (Button) v;
        if (!clickedButton.getText().toString().equals("")) return;

        if (player1Turn) {
            clickedButton.setText("X");
            moves.add("X" + getButtonIndex(clickedButton));
        } else {
            clickedButton.setText("O");
            moves.add("O" + getButtonIndex(clickedButton));
        }

        roundCount++;

        if (checkForWin()) {
            endGame(player1Turn ? player1Name : player2Name);
        } else if (roundCount == 9) {
            endGame("Draw");
        } else {
            player1Turn = !player1Turn;
            updateTurnText();
            if (isAI && !player1Turn) aiMove();
        }
    }

    private void aiMove() {
        if (player1Turn) return;

        new Handler().postDelayed(() -> {
            int i, j;
            Random rand = new Random();

            if ("Easy".equalsIgnoreCase(difficulty)) {
                do {
                    i = rand.nextInt(3);
                    j = rand.nextInt(3);
                } while (!buttons[i][j].getText().toString().equals(""));

            } else if ("Hard".equalsIgnoreCase(difficulty)) {
                int[] move = getBestMove();
                i = move[0];
                j = move[1];

            } else if ("Medium".equalsIgnoreCase(difficulty)) {
                if (rand.nextBoolean()) {
                    int[] move = getBestMove();
                    i = move[0];
                    j = move[1];
                } else {
                    do {
                        i = rand.nextInt(3);
                        j = rand.nextInt(3);
                    } while (!buttons[i][j].getText().toString().equals(""));
                }
            } else {
                do {
                    i = rand.nextInt(3);
                    j = rand.nextInt(3);
                } while (!buttons[i][j].getText().toString().equals(""));
            }

            buttons[i][j].performClick();
        }, 500);
    }

    private int[] getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setText("O");
                    int score = minimax(0, false);
                    buttons[i][j].setText("");
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing) {
        String result = checkWinnerSymbol();
        if (result != null) {
            if (result.equals("O")) return 10 - depth;
            if (result.equals("X")) return depth - 10;
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().toString().equals("")) {
                        buttons[i][j].setText("O");
                        int score = minimax(depth + 1, false);
                        buttons[i][j].setText("");
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().toString().equals("")) {
                        buttons[i][j].setText("X");
                        int score = minimax(depth + 1, true);
                        buttons[i][j].setText("");
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private String checkWinnerSymbol() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (!field[i][0].equals("") && field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]))
                return field[i][0];
            if (!field[0][i].equals("") && field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]))
                return field[0][i];
        }

        if (!field[0][0].equals("") && field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]))
            return field[0][0];
        if (!field[0][2].equals("") && field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]))
            return field[0][2];

        boolean empty = false;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (field[i][j].equals("")) empty = true;

        return empty ? null : "Draw";
    }

    private void updateTurnText() {
        textViewTurn.setText((player1Turn ? player1Name : player2Name) + "'s turn");
    }

    private boolean checkForWin() {
        return checkWinnerSymbol() != null && !checkWinnerSymbol().equals("Draw");
    }

    private void endGame(String winner) {
        String log = player1Name + " vs " + player2Name + " - Winner: " + winner;
        Intent intent = new Intent(this, EndScreenActivity.class);
        intent.putExtra("winner", winner);
        intent.putExtra("player1", player1Name);
        intent.putExtra("player2", player2Name);
        intent.putExtra("isAI", isAI);
        intent.putStringArrayListExtra("moves", moves);
        timerHandler.removeCallbacks(timerRunnable);
        intent.putExtra("duration", formatDuration(elapsedSeconds));
        startActivity(intent);
    }

    private String getButtonIndex(Button button) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == button) return i + "" + j;
            }
        }
        return "";
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        player1Turn = true;
        moves.clear();
        updateTurnText();
        if (isAI && !player1Turn) aiMove();
    }
    private String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

}
