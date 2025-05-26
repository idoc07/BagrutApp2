package com.example.bagrutapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class GameDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "game_logs.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_GAMES = "games";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MOVES = "moves";
    private static final String COLUMN_WINNER = "winner";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_GAMES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MOVES + " TEXT, "
                + COLUMN_WINNER + " TEXT, "
                + COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        onCreate(db);
    }

    public void addGame(String moves, String winner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOVES, moves);
        values.put(COLUMN_WINNER, winner);
        db.insert(TABLE_GAMES, null, values);
        db.close();
        deleteOldGames(); // Ensure only last 10 games are kept
    }

    private void deleteOldGames() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_GAMES + " WHERE id NOT IN (SELECT id FROM " + TABLE_GAMES + " ORDER BY " + COLUMN_TIMESTAMP + " DESC LIMIT 10);");
        db.close();
    }

    public List<String> getLastGames() {
        List<String> games = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GAMES + " ORDER BY " + COLUMN_TIMESTAMP + " DESC LIMIT 10", null);
        if (cursor.moveToFirst()) {
            do {
                String gameData = "Winner: " + cursor.getString(2) + " | Moves: " + cursor.getString(1);
                games.add(gameData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return games;
    }

    public Cursor getGameMoves(int gameId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT moves FROM games WHERE id = ?", new String[]{String.valueOf(gameId)});
    }
}
