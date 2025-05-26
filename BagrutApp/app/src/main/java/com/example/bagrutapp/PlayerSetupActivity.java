package com.example.bagrutapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class PlayerSetupActivity extends AppCompatActivity {

    private EditText etPlayer1, etPlayer2;
    private Button btnEnter, btnSwitch, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_setup);

        etPlayer1 = findViewById(R.id.et_player1);
        etPlayer2 = findViewById(R.id.et_player2);
        btnEnter = findViewById(R.id.btn_enter);
        btnSwitch = findViewById(R.id.btn_switch);
        btnExit = findViewById(R.id.btn_exit);

        btnEnter.setOnClickListener(v -> {
            Intent intent = new Intent(PlayerSetupActivity.this, GameActivity.class);
            intent.putExtra("isAI", false);
            intent.putExtra("player1", etPlayer1.getText().toString());
            intent.putExtra("player2", etPlayer2.getText().toString());
            startActivity(intent);
        });

        btnSwitch.setOnClickListener(v -> {
            String temp = etPlayer1.getText().toString();
            etPlayer1.setText(etPlayer2.getText().toString());
            etPlayer2.setText(temp);
        });

        btnExit.setOnClickListener(v -> finish());
    }
}