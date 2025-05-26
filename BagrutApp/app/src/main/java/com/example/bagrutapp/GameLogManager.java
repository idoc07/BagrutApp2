package com.example.bagrutapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameLogManager {
    private static final String PREFS_NAME = "GameLogs";
    private static final String KEY_LOGS = "Logs";

    private static final String FILE_NAME = "gamelogs.json";

    public static void saveGameLog(Context context, String title, List<String> moves, int durationInSeconds) {
        List<GameLogEntry> logs = loadGameLogObjects(context);

        logs.add(0, new GameLogEntry(title, moves, durationInSeconds));

        // שמירה רק של 10 אחרונים
        if (logs.size() > 10) {
            logs = logs.subList(0, 10);
        }

        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            FileWriter writer = new FileWriter(file);
            new Gson().toJson(logs, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<GameLogEntry> loadGameLogObjects(Context context) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) return new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<GameLogEntry> logs = new Gson().fromJson(reader, new TypeToken<List<GameLogEntry>>() {}.getType());
            reader.close();

            return logs != null ? logs : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();  // רושם את השגיאה ל־logcat
            return new ArrayList<>(); // מחזיר רשימה ריקה כדי למנוע קריסה
        }
    }
}
