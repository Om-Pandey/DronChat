package com.sendbird.android.sample.openchannel;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.sample.R;
import com.sendbird.android.sample.main.ConnectionManager;
import com.sendbird.android.sample.main.LoginActivity;
import com.sendbird.android.sample.main.SettingsActivity;
import com.sendbird.android.sample.utils.PreferenceUtils;

public class OpenChannelActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_channel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_open_channel);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            // Load list of Open Channels
            Fragment fragment = OpenChannelListFragment.newInstance();

            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();

            manager.beginTransaction()
                    .replace(R.id.container_open_channel, fragment)
                    .commit();
        }
    }

    interface onBackPressedListener {
        boolean onBack();
    }
    private onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_main)
        {
            Intent intent = new Intent(OpenChannelActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.disconnect)
        {
            disconnect();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
    private void disconnect() {
        SendBird.unregisterPushTokenAllForCurrentUser(new SendBird.UnregisterPushTokenHandler() {
            @Override
            public void onUnregistered(SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();

                    // Don't return because we still need to disconnect.
                }
                ConnectionManager.logout(new SendBird.DisconnectHandler() {
                    @Override
                    public void onDisconnected() {
                        PreferenceUtils.setConnected(false);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

}
