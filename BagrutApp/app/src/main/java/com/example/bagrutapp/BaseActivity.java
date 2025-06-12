package com.example.bagrutapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public abstract class BaseActivity extends AppCompatActivity {

    protected boolean isMusicPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        isMusicPlaying = isMusicServiceRunning();
        updateMusicIcon(menu.findItem(R.id.action_toggle_music));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle_music) {
            if (isMusicPlaying) {
                stopService(new Intent(this, MusicService.class));
            } else {
                startService(new Intent(this, MusicService.class));
            }
            isMusicPlaying = !isMusicPlaying;
            updateMusicIcon(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMusicIcon(MenuItem item) {
        item.setIcon(isMusicPlaying
                ? R.drawable.baseline_volume_up_24
                : R.drawable.baseline_volume_off_24);
    }

    private boolean isMusicServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MusicService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
