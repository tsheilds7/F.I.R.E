package com.onethatsinspired.fire.controllers.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.onethatsinspired.fire.R;

public class SplashActivityCreateAccount extends AppCompatActivity
{

    private FirebaseAuth mAuth;

    private static final int REQUEST_CODE = 101;

    private static final int RC_SIGN_IN = 9001;

    private static final String EMAIL = "email";

    EditText username;

    EditText email;

    EditText password;

    CallbackManager callbackManager;

    GoogleApiClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_create_account);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.editTextUsername);

        email = findViewById(R.id.editTextEmailPReset);

        password = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonSignUpc).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                emailSignUp(v);
            }
        });

        findViewById(R.id.buttonSignInc).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goBack(v);
            }
        });

    }

    public void goBack(View view)
    {
        Intent i = new Intent(this,SplashActivitySignIn.class);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void emailSignUp(final View view)
    {
        if (!email.getText().toString().isEmpty() && !username.getText().toString().isEmpty() && !password.getText().toString().isEmpty())
        {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {

                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username.getText().toString())
                                .build();

                        // Successfully account created
                        Log.d(null, "Accounted creation:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SplashActivityCreateAccount.this, "Successfull Account Creation", Toast.LENGTH_SHORT).show();

                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Log.d("null","Successful Account Creation");
                                }
                            }
                        });

                        goToHome();

                    }
                    else
                    {
                        // In case of acount creation failed.
                        Log.w(null, "Account creation:failure", task.getException());
                        Toast.makeText(SplashActivityCreateAccount.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast toast = Toast.makeText(this,"Please fill out all fields!",Toast.LENGTH_SHORT);

            View v = toast.getView();

            // v.setBackgroundColor(Color.parseColor("#6200EE"));

            //Gets the actual oval background of the Toast then sets the colour filter
            v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

            //Gets the TextView from the Toast so it can be edited
            TextView text = v.findViewById(android.R.id.message);

            text.setTextColor(Color.WHITE);

            text.setTextSize(18);

            toast.show();
        }

    }

    public  void goToHome()
    {
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }
}