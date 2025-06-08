package com.example.bagrutapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends BaseActivity implements View.OnClickListener {

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
    private boolean isAITurn = false;
    private String[][] virtualBoard = new String[3][3]; // לוח וירטואלי ל-minimax

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                virtualBoard[i][j] = ""; // אתחול לוח וירטואלי
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
        if (isAITurn) return;

        Button clickedButton = (Button) v;
        if (!clickedButton.getText().toString().equals("")) return;

        String symbol = player1Turn ? "X" : "O";
        clickedButton.setText(symbol);

        // עדכון לוח וירטואלי
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j] == clickedButton) {
                    virtualBoard[i][j] = symbol;
                    moves.add(symbol + i + j);
                }
            }
        }

        roundCount++;

        if (checkForWin()) {
            endGame(player1Turn ? player1Name : player2Name);
        } else if (roundCount == 9) {
            endGame("Draw");
        } else {
            player1Turn = !player1Turn;
            updateTurnText();

            if (isAI && !player1Turn) {
                aiMove();
            }
        }
    }

    private void aiMove() {
        isAITurn = true;
        setButtonsEnabled(false);

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int[] move = getAIMove();

            runOnUiThread(() -> {
                int i = move[0];
                int j = move[1];
                buttons[i][j].setText("O");  // עדכון ישירות
                virtualBoard[i][j] = "O";    // עדכון ישירות
                moves.add("O" + i + j);

                roundCount++;

                if (checkForWin()) {
                    endGame(player2Name);
                } else if (roundCount == 9) {
                    endGame("Draw");
                } else {
                    player1Turn = true;
                    updateTurnText();
                    isAITurn = false;
                    setButtonsEnabled(true);
                }
            });
        }).start();
    }

    private int[] getAIMove() {
        if (difficulty.equals("Easy")) {
            ArrayList<int[]> emptyCells = getEmptyCells();
            return emptyCells.get(new Random().nextInt(emptyCells.size()));
        } else if (difficulty.equals("Medium")) {
            if (new Random().nextBoolean()) {
                return getAIMove("Easy");
            } else {
                return getBestMove();
            }
        } else {
            return getBestMove();
        }
    }

    private int[] getAIMove(String mode) {
        ArrayList<int[]> emptyCells = getEmptyCells();
        return emptyCells.get(new Random().nextInt(emptyCells.size()));
    }

    private ArrayList<int[]> getEmptyCells() {
        ArrayList<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (virtualBoard[i][j].equals("")) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }
        return emptyCells;
    }

    private int[] getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (virtualBoard[i][j].equals("")) {
                    virtualBoard[i][j] = "O";
                    int score = minimax(0, false);
                    virtualBoard[i][j] = "";

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
        String result = checkWinnerSymbol(virtualBoard);
        if (result != null) {
            if (result.equals("O")) return 10 - depth;
            if (result.equals("X")) return depth - 10;
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (virtualBoard[i][j].equals("")) {
                        virtualBoard[i][j] = "O";
                        int score = minimax(depth + 1, false);
                        virtualBoard[i][j] = "";
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (virtualBoard[i][j].equals("")) {
                        virtualBoard[i][j] = "X";
                        int score = minimax(depth + 1, true);
                        virtualBoard[i][j] = "";
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    private String checkWinnerSymbol(String[][] board) {
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].equals("") && board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]))
                return board[i][0];
            if (!board[0][i].equals("") && board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]))
                return board[0][i];
        }

        if (!board[0][0].equals("") && board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]))
            return board[0][0];
        if (!board[0][2].equals("") && board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]))
            return board[0][2];

        boolean empty = false;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j].equals("")) empty = true;

        return empty ? null : "Draw";
    }

    private boolean checkForWin() {
        String result = checkWinnerSymbol(virtualBoard);
        return result != null && !result.equals("Draw");
    }

    private void updateTurnText() {
        textViewTurn.setText((player1Turn ? player1Name : player2Name) + "'s turn");
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
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                virtualBoard[i][j] = "";
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

    private void setButtonsEnabled(boolean enabled) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().toString().equals("")) {
                    buttons[i][j].setEnabled(enabled);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
