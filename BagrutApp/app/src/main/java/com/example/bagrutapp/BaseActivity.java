// BaseActivity.java
package com.example.bagrutapp;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected boolean isMusicPlaying = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // קובעים אייקון התחלתי בהתאם ל־isMusicPlaying
        MenuItem musicItem = menu.findItem(R.id.action_toggle_music);
        if (isMusicPlaying) {
            musicItem.setIcon(R.drawable.baseline_volume_up_24);
        } else {
            musicItem.setIcon(R.drawable.baseline_volume_off_24);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle_music) {
            if (isMusicPlaying) {
                stopService(new Intent(this, MusicService.class));
                item.setIcon(R.drawable.baseline_volume_off_24);
            } else {
                startService(new Intent(this, MusicService.class));
                item.setIcon(R.drawable.baseline_volume_up_24);
            }
            isMusicPlaying = !isMusicPlaying;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
