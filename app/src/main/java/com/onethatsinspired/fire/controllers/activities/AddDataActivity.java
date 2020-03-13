package com.onethatsinspired.fire.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.onethatsinspired.fire.R;
import com.onethatsinspired.fire.databinding.ActivityAdddataBinding;
import com.onethatsinspired.fire.repositories.FIreRepo;

public class AddDataActivity extends AppCompatActivity
{
    private Button buttonSubmit;

    private EditText editTextDescription;

    private EditText editTextLink;

    private EditText editTextName;

    private FirebaseAuth  mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseFunctions firebaseFunctions;

    public FirebaseUser currentUser;

    private FloatingActionButton buttonBack;

    public RatingBar ratingBar;

    private String collection;

    private TabLayout tabLayout;

    private TextView textViewMode;

    private ActivityAdddataBinding activityAdddataBinding;

    private  FIreRepo fIreRepo;

    Client client;

    Index index;

    @Override
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


       //activityAdddataBinding = ActivityAdddataBinding.inflate(getLayoutInflater());

        //View view = activityAdddataBinding.getRoot();

        setContentView(R.layout.activity_adddata);


        client = new Client("4FW4PHPOL5", "709954ad213a5f950da58271d7542581");

        index = client.getIndex("podcast");

        fIreRepo = new FIreRepo();



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
                        index = client.getIndex("podcast");
                        break;
                    case(1):
                        textViewMode.setText("Add Youtube Channel");
                        collection = "youtube";
                        index = client.getIndex("youtube");
                        break;
                    case(2):
                        textViewMode.setText("Add Book");
                        collection = "book";
                        index = client.getIndex("book");
                        break;
                    case(3):
                        textViewMode.setText("Add Pro");
                        collection = "pro";
                        index = client.getIndex("pro");
                        break;
                    case(4):
                        textViewMode.setText("Add Blog");
                        collection = "blog";
                        index = client.getIndex("blog");
                        break;
                        default:
                            textViewMode.setText("Add Podcast");
                            collection = "podcast";
                            index = client.getIndex("podcast");
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

              fIreRepo.submitToDatabase(AddDataActivity.this, collection, index, editTextName.getText().toString(),editTextDescription.getText().toString(),editTextLink.getText().toString(),String.valueOf((int)ratingBar.getRating()), currentUser.getEmail(), collection);

              goBackToHome();

            }
        });

        /** Listeners **/

    }

    private void goBackToHome()
    {
        Intent i = new Intent(this, HomeActivity.class);

        startActivity(i);
    }

}