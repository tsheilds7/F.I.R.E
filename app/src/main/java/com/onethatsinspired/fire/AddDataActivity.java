package com.onethatsinspired.fire;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;
import com.onethatsinspired.fire.databinding.ActivityAdddataBinding;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AddDataActivity extends AppCompatActivity
{
    private Button buttonSubmit;

    private EditText editTextDescription;

    private EditText editTextLink;

    private EditText editTextName;

    private FirebaseAuth  mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseFunctions firebaseFunctions;

    private FirebaseUser currentUser;

    private FloatingActionButton buttonBack;

    private RatingBar ratingBar;

    private String collection;

    private TabLayout tabLayout;

    private TextView textViewMode;

    private ActivityAdddataBinding activityAdddataBinding;


    @Override
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


       //activityAdddataBinding = ActivityAdddataBinding.inflate(getLayoutInflater());

        //View view = activityAdddataBinding.getRoot();

        setContentView(R.layout.activity_adddata);



        /**  Assignments **/

        buttonBack = findViewById(R.id.buttonBack);

        buttonSubmit = findViewById(R.id.buttonSubmit);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        collection = "podcast";

        editTextDescription = findViewById(R.id.editDescription);

        editTextLink = findViewById(R.id.editLink);

        editTextName = findViewById(R.id.editName);

        firebaseFunctions = FirebaseFunctions.getInstance();



        ratingBar = findViewById(R.id.ratingBar);

        tabLayout = findViewById(R.id.tabs);

        textViewMode = findViewById(R.id.text_Mode);

        /**  Assignments **/



        /** Listeners **/

        buttonSubmit.setOnClickListener(new android.view.View.OnClickListener()
        {
            @Override
            public void onClick(android.view.View v)
            {
                submitToDatabase();
            }
        });



        buttonBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goBackToHome();
            }
        });



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                switch(tabLayout.getSelectedTabPosition())
                {
                    case(0):
                        textViewMode.setText("Add Podcast");
                        collection = "podcast";
                        break;
                    case(1):
                        textViewMode.setText("Add Youtube Channel");
                        collection = "youtube";
                        break;
                    case(2):
                        textViewMode.setText("Add Book");
                        collection = "book";
                        break;
                    case(3):
                        textViewMode.setText("Add Pro");
                        collection = "pro";
                        break;
                    case(4):
                        textViewMode.setText("Add Blog");
                        collection = "blog";
                        break;
                        default:
                            textViewMode.setText("Add Podcast");
                            collection = "podcast";
                            break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });



        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

               submitToDatabase();

            }
        });

        /** Listeners **/

    }



    private void submitToDatabase()
    {
        Map<String, Object> dbInput = new HashMap<>();

        dbInput.put("name", editTextName.getText().toString());

        dbInput.put("about", editTextDescription.getText().toString());

        dbInput.put("link", editTextLink.getText().toString());

        dbInput.put("avgrating", String.valueOf((int)ratingBar.getRating()));

        dbInput.put("addedbyuser", currentUser.getEmail());

        dbInput.put("type", collection);



        db.collection(collection).add(dbInput).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
        {
            @Override
            public void onSuccess(final DocumentReference documentReference)
            {



                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {

                        Map<String, Object> dbInputRating = new HashMap<>();

                        dbInputRating.put("rate", String.valueOf((int)ratingBar.getRating()));
                        dbInputRating.put("addedbyuser", currentUser.getEmail());

                        db.collection(collection).document(documentSnapshot.getId()).collection("ratings").add(dbInputRating).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                        {
                            @Override
                            public void onSuccess(DocumentReference documentReference)
                            {
                                Toast toast = Toast.makeText(getApplicationContext(),"Rating Submitted!",Toast.LENGTH_SHORT);
                                View v = toast.getView();
                                // v.setBackgroundColor(Color.parseColor("#6200EE"));

                                //Gets the actual oval background of the Toast then sets the colour filter
                                v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

                                //Gets the TextView from the Toast so it can be editted
                                TextView text = v.findViewById(android.R.id.message);
                                text.setTextColor(Color.WHITE);
                                text.setTextSize(24);
                                toast.show();

                                goBackToHome();
                            }
                        });
                    }
                });




            }
        })
                .addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.v(null,"Failed");
            }
        });

    }



    private void goBackToHome()
    {
        Intent i = new Intent(this, HomeActivity.class);

        startActivity(i);
    }



}
