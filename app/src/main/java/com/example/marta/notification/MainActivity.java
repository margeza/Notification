package com.example.marta.notification;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        Log.d("Token", refreshedToken);
    }

    public void mapIntent(View view){

        String url = "https://www.google.com/maps/d/edit?mid=19oWMd_HImbYUdvIIMAt71fUbO5w&ll=54.35226261280995%2C18.651151300000038&z=15";
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void mapActivity (View view) {
        intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
