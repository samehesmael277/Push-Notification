package com.sameh.pushnotification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.sameh.pushnotification.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final String CHANNEL_ID = "push_channel_id";

    static final String TAG = "applicationTAG";
    static final String USER_DATA_KEY = "user_data_key";

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!hasPermissions())
            requestNotificationPermission();
        setActions();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void setActions() {
        binding.btnPushNotification.setOnClickListener(view -> {
            if (hasPermissions()) {
                checkPushNotificationValidation();
            } else {
                requestNotificationPermission();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void checkPushNotificationValidation() {
        String data = Objects.requireNonNull(binding.etData.getText()).toString();
        if (data.isEmpty()) {
            showToast("You should enter data");
        } else {
            pushNotification();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void pushNotification() {
        createNotificationChannel();
        showNotification();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void showNotification() {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

            String data = Objects.requireNonNull(binding.etData.getText()).toString();
            Log.e(TAG, "showNotification: " + data);

            Intent intent = new Intent(this, NotificationActivity.class);
            intent.putExtra(USER_DATA_KEY, data);

            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_ONE_SHOT);

            builder.setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Push Notification")
                    .setContentText("New Data Sent")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            RemoteViews customLayout = new RemoteViews(getPackageName(), R.layout.notification_layout);
            customLayout.setTextViewText(R.id.tv_notification_title, "Push Notification");
            customLayout.setTextViewText(R.id.tv_notification_des, "New Data Sent");
            customLayout.setImageViewResource(R.id.iv_notification_ic, R.drawable.ic_notification);

            builder.setCustomContentView(customLayout);

            // Show the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermission();
                return;
            }
            notificationManager.notify(0, builder.build());
        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Push Notification Channel";
            String description = "Push Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showToast(String toastMessage) {
        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("Permission Granted");
                Log.d(TAG, "Permission Granted");
            } else {
                showToast("Permission Denied");
                Log.d(TAG, "Permission Denied");
            }
        }
    }
}