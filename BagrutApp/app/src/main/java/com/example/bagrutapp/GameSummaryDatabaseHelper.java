package com.example.bagrutapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;

public class GameSummaryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "game_summaries.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "summaries";

    public GameSummaryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "player1 TEXT, " +
                "player2 TEXT, " +
                "winner TEXT, " +
                "duration TEXT, " +
                "moves TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertGameSummary(GameSummary summary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("player1", summary.getPlayer1());
        values.put("player2", summary.getPlayer2());
        values.put("winner", summary.getWinner());
        values.put("duration", summary.getDuration());
        values.put("moves", summary.getMoves());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<GameSummary> getAllGameSummaries() {
        List<GameSummary> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM summaries ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            do {
                String player1 = cursor.getString(cursor.getColumnIndexOrThrow("player1"));
                String player2 = cursor.getString(cursor.getColumnIndexOrThrow("player2"));
                String winner = cursor.getString(cursor.getColumnIndexOrThrow("winner"));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow("duration"));
                String moves = cursor.getString(cursor.getColumnIndexOrThrow("moves"));

                GameSummary summary = new GameSummary(player1, player2, winner, duration, moves);
                list.add(summary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}
