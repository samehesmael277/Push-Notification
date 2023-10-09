package com.sameh.pushnotification;

import static com.sameh.pushnotification.MainActivity.TAG;
import static com.sameh.pushnotification.MainActivity.USER_DATA_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.sameh.pushnotification.databinding.ActivityNotificationBinding;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String userData = intent.getStringExtra(USER_DATA_KEY);
        Log.e(TAG, "NotificationActivity: " + intent);
        Log.e(TAG, "NotificationActivity: " + intent.getExtras());
        Log.e(TAG, "NotificationActivity: " + userData);
        if (userData != null && !userData.isEmpty()) {
            binding.tvDataSent.setText(userData);
        }
    }
}