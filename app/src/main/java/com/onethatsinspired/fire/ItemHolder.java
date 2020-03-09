package com.onethatsinspired.fire;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

class ItemHolder extends RecyclerView.ViewHolder
{

    RatingBar ratingBarAverage;

    String ultraAbout;

    String ultraId;

    String ultraLink;

    String ultraName;

    String ultraRating;

    String ultraType;

    TextView textViewName;

    HomeActivity homeActivity;

    RatingBar ratingBarAvgInfo;

    View view;

    public ItemHolder(final View itemView)
    {

        super(itemView);

        resetData(itemView);

        textViewName = itemView.findViewById(R.id.itemName);

        ratingBarAverage = itemView.findViewById(R.id.ratingBar);

        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                showPopup(v);
            }
        });

        homeActivity = (HomeActivity)itemView.getContext();

    }

    public  void resetData(View itemView)
    {
        view = itemView;
    }


    public  void showPopup(final View view)
    {

        Context mContext = getApplicationContext();

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        final TabLayout tabLayout = view.getRootView().findViewById(R.id.tabs);

        tabLayout.getTabAt(0).view.setEnabled(false);

        tabLayout.getTabAt(1).view.setEnabled(false);

        tabLayout.getTabAt(2).view.setEnabled(false);

        tabLayout.getTabAt(3).view.setEnabled(false);

        tabLayout.getTabAt(4).view.setEnabled(false);

        view.setVisibility(View.INVISIBLE);

        view.getRootView().findViewById(R.id.action_add).setEnabled(false);

        view.getRootView().findViewById(R.id.action_settings).setEnabled(false);

        //view.getRootView().setAlpha(0.35f);

        homeActivity.coordinatorLayout.setAlpha(0.35f);

        // Inflate the custom layout/view
        final View popupView = inflater.inflate(R.layout.layout_itemdetail,null);

        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21)
        {
            popupWindow.setElevation(50.0f);
        }

        Typeface typeface = ResourcesCompat.getFont(view.getRootView().getContext(), R.font.alegreya_black_italic);

        // Finally, show the popup window at the center location of root relative layout
        popupWindow.showAtLocation(view, Gravity.CENTER,00,0);

        Button fab = popupView.findViewById(R.id.fabClose);

        TextView textViewLiink = popupView.findViewById(R.id.textViewLink);
        textViewLiink.setTypeface(typeface);

        ratingBarAvgInfo = popupView.findViewById(R.id.ratingBarAverageInfo);

        TextView textViewAbout = popupView.findViewById(R.id.textViewDescription);
        textViewAbout.setTypeface(typeface);

        TextView textViewName = popupView.findViewById(R.id.textViewName);
        textViewName.setTypeface(typeface);

        TextView textViewRateThis = popupView.findViewById(R.id.textViewRateThis);
        textViewRateThis.setTypeface(typeface);

        Button buttonSubmitRating = popupView.findViewById(R.id.buttonSubmitRating);
        buttonSubmitRating.setTypeface(typeface);

        final String link = ultraLink;

        final String rating = ultraRating;

        final String about = ultraAbout;

        final String name = ultraName;

        textViewName.setText(name);

        textViewLiink.setText(link);

        textViewAbout.setText(about);

        ratingBarAvgInfo.setRating(Integer.parseInt(rating));

        textViewLiink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse( "https://" +   link); // missing 'http://' will cause crashed

                Context context = v.getContext();

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TabLayout tabLayout = homeActivity.tabLayout;

                resetSettings(view,tabLayout);

                popupWindow.dismiss();
            }
        });

        popupView.findViewById(R.id.buttonSubmitRating).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore.collection(ultraType).whereEqualTo("name",ultraName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                    {

                        DocumentReference documentReference =  firebaseFirestore.collection(ultraType).document(queryDocumentSnapshots.getDocuments().get(0).getId());

                        RatingBar newRatingBar = popupView.findViewById(R.id.ratingBarSubmit);

                        addRating(documentReference,(int)newRatingBar.getRating());

                        popupWindow.dismiss();

                        TabLayout tabLayout = homeActivity.tabLayout;

                        resetSettings(view,tabLayout);

                    }
                });
            }
        });
    }



    public  void resetSettings(View view, TabLayout tabLayout)
    {
        view.setVisibility(View.VISIBLE);

        //view.getRootView().setAlpha(1.0f);

        homeActivity.coordinatorLayout.setAlpha(1.0f);

        tabLayout.getTabAt(0).view.setEnabled(true);

        tabLayout.getTabAt(1).view.setEnabled(true);

        tabLayout.getTabAt(2).view.setEnabled(true);

        tabLayout.getTabAt(3).view.setEnabled(true);

        tabLayout.getTabAt(4).view.setEnabled(true);

        homeActivity.add_button.setEnabled(true);

        homeActivity.settings_button.setEnabled(true);
    }



    private void addRating(final DocumentReference documentReference, final float rating)
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final String profile = firebaseUser.getEmail();

        Task<QuerySnapshot> addedbyuser = documentReference.collection("ratings").get();

        addedbyuser.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
        {
            boolean alreadyAdded = false;

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {

                for(QueryDocumentSnapshot q : queryDocumentSnapshots)
                {
                    String name = (String)q.get("addedbyuser");

                    if( profile.equals(q.get("addedbyuser")))
                    {
                        alreadyAdded = true;

                        Log.e("Database","Rating already left by user");

                        Toast toast = Toast.makeText(getApplicationContext(),"Sorry you've already left a review!",Toast.LENGTH_SHORT);

                        View v = toast.getView();

                        // v.setBackgroundColor(Color.parseColor("#6200EE"));

                        //Gets the actual oval background of the Toast then sets the colour filter
                        v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                        //Gets the TextView from the Toast so it can be edited
                        TextView text = v.findViewById(android.R.id.message);

                        text.setTextColor(Color.WHITE);

                        text.setTextSize(24);

                        toast.show();

                    }

                }

                if(!alreadyAdded)
                {
                    // Create reference for new rating, for use inside the transaction
                    final CollectionReference ratingRef = documentReference.collection("ratings");

                    final  int  avgRating;

                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                    {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot)
                        {

                            documentSnapshot.get("avgrating");

                            documentReference.collection("ratings").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                            {
                                @Override
                                public void onSuccess(final QuerySnapshot queryDocumentSnapshots)
                                {


                                    float sum = 0;

                                    int numberOfData = queryDocumentSnapshots.size();

                                    if (numberOfData > 0)
                                    {

                                        for(QueryDocumentSnapshot q : queryDocumentSnapshots)
                                        {

                                            sum =  sum + Integer.parseInt(q.get("rate").toString());

                                        }

                                    }

                                    final float rate;

                                    rate = (sum + rating) / (numberOfData + 1);

                                    Map<String, Object> ratingInput = new HashMap<>();

                                    ratingInput.put("rate", String.valueOf((int)rating));
                                    ratingInput.put("addedbyuser", firebaseUser.getEmail());

                                    ratingRef.add(ratingInput).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                                    {

                                        @Override
                                        public void onSuccess(DocumentReference documentReference)
                                        {

                                            documentReference.getParent().getParent().update("avgrating", String.valueOf((int)rate)).addOnSuccessListener(new OnSuccessListener<Void>()
                                            {

                                                @Override
                                                public void onSuccess(Void aVoid)
                                                {
                                                    Toast toast = Toast.makeText(getApplicationContext(),"Rating Submitted!",Toast.LENGTH_SHORT);

                                                    View v = toast.getView();

                                                    // v.setBackgroundColor(Color.parseColor("#6200EE"));

                                                    //Gets the actual oval background of the Toast then sets the colour filter

                                                    v.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

                                                    //Gets the TextView from the Toast so it can be edited

                                                    TextView text = v.findViewById(android.R.id.message);

                                                    text.setTextColor(Color.WHITE);

                                                    text.setTextSize(24);

                                                    toast.show();

                                                    homeActivity.resetRecyclerAdapter(getLayoutPosition());

                                                    ratingBarAvgInfo.setRating(Integer.parseInt(ultraRating));




                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });



    }

}
