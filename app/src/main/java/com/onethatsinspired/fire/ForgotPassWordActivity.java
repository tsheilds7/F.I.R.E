package com.onethatsinspired.fire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ForgotPassWordActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_pass_word);

       final FirebaseAuth auth = FirebaseAuth.getInstance();

       final EditText editText = findViewById(R.id.editTextEmailPReset);

       final String emailAddress = editText.getText().toString();

        findViewById(R.id.buttonResetPassword).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                auth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Log.d(null,"Email sent.");
                            Toast toast = Toast.makeText(getApplicationContext(),"Email sent!",Toast.LENGTH_SHORT);
                            View v = toast.getView();
                            // v.setBackgroundColor(Color.parseColor("#6200EE"));

                            //Gets the actual oval background of the Toast then sets the colour filter
                            v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

                            //Gets the TextView from the Toast so it can be editted
                            TextView text = v.findViewById(android.R.id.message);
                            text.setTextColor(Color.WHITE);
                            text.setTextSize(24);
                            toast.show();

                            goBack();
                        }
                    }
                });
            }
        });


    }

    public  void goBack()
    {
        Intent i = new Intent(this, SplashActivitySignIn.class);
        startActivity(i);
    }

}
