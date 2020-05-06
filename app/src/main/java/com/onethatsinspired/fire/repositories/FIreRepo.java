package com.onethatsinspired.fire.repositories;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.onethatsinspired.fire.adapters.AlgoliaRecyclerAdapter;
import com.onethatsinspired.fire.adapters.HomePagerAdapter;
import com.onethatsinspired.fire.controllers.activities.AddDataActivity;
import com.onethatsinspired.fire.controllers.activities.HomeActivity;
import com.onethatsinspired.fire.viewmodels.FireViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FIreRepo
{

    public FirestoreRecyclerOptions<FireViewModel> setUpData(int tab)
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection("podcast"); ;

        switch (tab)
        {

            case 0:
                collectionReference = db.collection("podcast");
                break;
            case 1:
                collectionReference = db.collection("youtube");
                break;
            case 2:
                collectionReference = db.collection("book");
                break;
            case 3:
                collectionReference = db.collection("pro");
                break;
            case 4:
                collectionReference = db.collection("blog");
                break;

        }

        Query query =  collectionReference.orderBy("avgrating", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<FireViewModel> options = new FirestoreRecyclerOptions.Builder<FireViewModel>().setQuery(query,FireViewModel.class).build();

        return options;

    }

    public void submitToDatabase(final AddDataActivity addDataActivity,final String collection,Index index,String name, String about, String link, String avgrating, String addedbyuser, String type)
    {
        final Map<String, Object> dbInput = new HashMap<>();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        dbInput.put("name", name);

        dbInput.put("about", about);

        dbInput.put("link", link);

        dbInput.put("avgrating", avgrating);

        dbInput.put("addedbyuser", addedbyuser);

        dbInput.put("type", type);

        Random random = new Random();

        final String randomNumber = String.valueOf(random.nextInt(500000000));

        index.addObjectAsync(new JSONObject(dbInput), randomNumber, null);

        dbInput.put("objectid", randomNumber);

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

                        dbInputRating.put("rate", String.valueOf((int)addDataActivity.ratingBar.getRating()));
                        dbInputRating.put("addedbyuser", addDataActivity.currentUser.getEmail());

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

    public void addRating(final HomeActivity homeActivity, final DocumentReference documentReference, final float rating)
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
                        public void onSuccess(final DocumentSnapshot documentSnapshot)
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

                                    final Map<String, Object> ratingInput = new HashMap<>();

                                    ratingInput.put("rate", String.valueOf((int)rating));
                                    ratingInput.put("addedbyuser", firebaseUser.getEmail());

                                    ratingRef.add(ratingInput).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                                    {

                                        @Override
                                        public void onSuccess(final DocumentReference documentReference)
                                        {

                                            documentReference.getParent().getParent().update("avgrating", String.valueOf((int)rate)).addOnSuccessListener(new OnSuccessListener<Void>()
                                            {
                                                String objectID;
                                                @Override
                                                public void onSuccess(Void aVoid)
                                                {


                                                    objectID = documentSnapshot.get("objectid").toString();
                                                    try
                                                    {
                                                            homeActivity.index.partialUpdateObjectAsync(new JSONObject("{\"avgrating\" : " + (int)rate + " }"), objectID,true,null);                                                            }
                                                    catch(JSONException e)
                                                    {
                                                        e.printStackTrace();
                                                    }







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

    AlgoliaRecyclerAdapter adapter;

    public void performAlgoliaSearch(final int tabPosition, Index index, String query, final HomePagerAdapter homeAdapter)
    {

        com.algolia.search.saas.Query query1 = new com.algolia.search.saas.Query(query).setAttributesToRetrieve("name","about","type","link","avgrating").setHitsPerPage(50);

        index.searchAsync(query1, new CompletionHandler()
        {
            @Override
            public void requestCompleted(@Nullable JSONObject content, @Nullable AlgoliaException e)
            {
                Log.e("Search", content.toString());

                switch (tabPosition)
                {
                    case 0:
                        homeAdapter.podcastFragment.fireViewModelList.clear();
                        //homeAdapter.podcastFragment.fIreRepo.adapter.notifyDataSetChanged();

                        try
                        {
                            JSONArray hits = content.getJSONArray("hits");

                            for(int i = 0; i < hits.length(); i++)
                            {
                                JSONObject jsonObject = hits.getJSONObject(i);
                                String about = jsonObject.getString("about");
                                String avgrating = jsonObject.getString("avgrating");
                                String link = jsonObject.getString("link");
                                String name = jsonObject.getString("name");
                                String type = jsonObject.getString("type");
                                FireViewModel fireViewModel  = new FireViewModel(about,avgrating,link,name,type);

                                homeAdapter.podcastFragment.fireViewModelList.add(fireViewModel);
                            }

                            adapter = new AlgoliaRecyclerAdapter(getApplicationContext(), homeAdapter.podcastFragment.fireViewModelList);

                            homeAdapter.podcastFragment.recyclerView.setAdapter(adapter);

                        }
                        catch(JSONException ePodcast)
                        {
                            ePodcast.printStackTrace();
                        }



                        break;
                    case 1:
                        homeAdapter.youtubeFragment.fireViewModelList.clear();
                        //homeAdapter.youtubeFragment.fIreRepo.adapter.notifyDataSetChanged();

                        try
                        {
                            JSONArray  hits = content.getJSONArray("hits");

                            for(int i = 0; i < hits.length(); i++)
                            {
                                JSONObject jsonObject = hits.getJSONObject(i);
                                String about = jsonObject.getString("about");
                                String avgrating = jsonObject.getString("avgrating");
                                String link = jsonObject.getString("link");
                                String name = jsonObject.getString("name");
                                String type = jsonObject.getString("type");
                                FireViewModel fireViewModel  = new FireViewModel(about,avgrating,link,name,type);

                                homeAdapter.youtubeFragment.fireViewModelList.add(fireViewModel);
                            }
                            adapter = new AlgoliaRecyclerAdapter(getApplicationContext(), homeAdapter.youtubeFragment.fireViewModelList);

                            homeAdapter.youtubeFragment.recyclerView.setAdapter(adapter);
                        }
                        catch(JSONException eYoutube)
                        {
                            eYoutube.printStackTrace();
                        }
                        break;
                    case 2:
                        homeAdapter.booksFragment.fireViewModelList.clear();
                        // homeAdapter.booksFragment.fIreRepo.adapter.notifyDataSetChanged();

                        try
                        {
                            JSONArray  hits = content.getJSONArray("hits");

                            for(int i = 0; i < hits.length(); i++)
                            {
                                JSONObject jsonObject = hits.getJSONObject(i);
                                String about = jsonObject.getString("about");
                                String avgrating = jsonObject.getString("avgrating");
                                String link = jsonObject.getString("link");
                                String name = jsonObject.getString("name");
                                String type = jsonObject.getString("type");

                                FireViewModel fireViewModel  = new FireViewModel(about,avgrating,link,name,type);

                                homeAdapter.booksFragment.fireViewModelList.add(fireViewModel);
                            }
                            adapter = new AlgoliaRecyclerAdapter(getApplicationContext(), homeAdapter.booksFragment.fireViewModelList);

                            homeAdapter.booksFragment.recyclerView.setAdapter(adapter);
                        }
                        catch(JSONException eBooks)
                        {
                            eBooks.printStackTrace();
                        }

                        break;
                    case 3:
                        homeAdapter.prosFragment.fireViewModelList.clear();
                        //homeAdapter.prosFragment.fIreRepo.adapter.notifyDataSetChanged();

                        try
                        {
                            JSONArray  hits = content.getJSONArray("hits");

                            for(int i = 0; i < hits.length(); i++)
                            {
                                JSONObject jsonObject = hits.getJSONObject(i);
                                String about = jsonObject.getString("about");
                                String avgrating = jsonObject.getString("avgrating");
                                String link = jsonObject.getString("link");
                                String name = jsonObject.getString("name");
                                String type = jsonObject.getString("type");
                                FireViewModel fireViewModel  = new FireViewModel(about,avgrating,link,name,type);

                                homeAdapter.prosFragment.fireViewModelList.add(fireViewModel);
                            }
                            adapter = new AlgoliaRecyclerAdapter(getApplicationContext(), homeAdapter.prosFragment.fireViewModelList);

                            homeAdapter.prosFragment.recyclerView.setAdapter(adapter);
                        }
                        catch(JSONException ePros)
                        {
                            ePros.printStackTrace();
                        }


                        break;
                    case 4:
                        homeAdapter.blogsFragment.fireViewModelList.clear();
                        //homeAdapter.blogsFragment.fIreRepo.adapter.notifyDataSetChanged();

                        try
                        {
                            JSONArray  hits = content.getJSONArray("hits");

                            for(int i = 0; i < hits.length(); i++)
                            {
                                JSONObject jsonObject = hits.getJSONObject(i);
                                String about = jsonObject.getString("about");
                                String avgrating = jsonObject.getString("avgrating");
                                String link = jsonObject.getString("link");
                                String name = jsonObject.getString("name");
                                String type = jsonObject.getString("type");
                                FireViewModel fireViewModel  = new FireViewModel(about,avgrating,link,name,type);

                                homeAdapter.blogsFragment.fireViewModelList.add(fireViewModel);
                            }
                            adapter = new AlgoliaRecyclerAdapter(getApplicationContext(), homeAdapter.blogsFragment.fireViewModelList);

                            homeAdapter.blogsFragment.recyclerView.setAdapter(adapter);
                        }
                        catch(JSONException eBlogs)
                        {
                            eBlogs.printStackTrace();
                        }

                        break;
                }


            }
        });




    }

}