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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onethatsinspired.fire.R;


import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SplashActivitySignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{

    private FirebaseAuth mAuth;

    private static final int REQUEST_CODE = 101;

    private static final int RC_SIGN_IN = 9001;

    private static final String EMAIL = "email";

    Button loginButton;

    EditText email;

    EditText password;

    CallbackManager callbackManager;

    GoogleApiClient mGoogleSignInClient;

    MaterialButton materialLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_sign_in);

        // loginButton = (LoginButton) findViewById(R.id.login_button);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editTextEmailPReset);
        password = findViewById(R.id.editTextPassword);

        // Set the dimensions of the sign-in button.
        Button buttonSignIn = findViewById(R.id.buttonSignIn);



        buttonSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToSignUp();
            }
        });


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleSignInClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();


    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (!(user == null))
        {

        }

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


    }

    private List<AuthUI.IdpConfig> getAuthProviderList()
    {
        List<AuthUI.IdpConfig> providers = new ArrayList<>();

        // providers.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());

        //providers.add(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

        // providers.add(new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build());

        return providers;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == ResultCodes.OK)
            {
                return;
            }
         else {
                if (response == null)
                {
                    // if user cancelled Sign-in
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK)
                {
                    // When device has no network connection
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR)
                {
                    // When unknown error occurred
                    return;
                }
            }
        }
    }

    public void emailSignIn(View view)
    {
        if(!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty())
        {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        // Successful signin
                        Log.d(null, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SplashActivitySignIn.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        goToHome();
                    } else {
                        // failed for some reason
                        Log.w(null, "signInWithEmail:failure", task.getException());
                        Toast.makeText(SplashActivitySignIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        Log.d(null, "Connection Failed because: " + connectionResult);
    }

    public  void goToHome()
    {
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }

    public  void goToSignUp()
    {
        Intent i = new Intent(this,SplashActivityCreateAccount.class);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public  void passwordReset(View view)
    {
        Intent i = new Intent(this,ForgotPassWordActivity.class);
        startActivity(i);
    }

}