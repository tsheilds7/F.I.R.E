package com.onethatsinspired.fire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onethatsinspired.fire.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;

    private AdView adview;

    FloatingActionButton floatingActionButton;

    private ActivitySettingsBinding activitySettingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());

        View view = activitySettingsBinding.getRoot();

        setContentView(view);

        setContentView(R.layout.activity_settings);

        adview = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();

        adview.loadAd((adRequest));

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        TextView textView = findViewById(R.id.textViewCurrentEmail);

        textView.setText(user.getEmail());

        floatingActionButton = findViewById(R.id.fabBack);

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goBackToHome();
            }
        });

        Button buttonLogOut = findViewById(R.id.buttonLogOut);

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });


    }

    public  void goBackToHome()
    {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        Log.e("search", "fab clicked");
    }

    public  void signOut()
    {
        mAuth.signOut();
        Intent i = new Intent(this, SplashActivitySignIn.class);
        startActivity(i);
    }
}
