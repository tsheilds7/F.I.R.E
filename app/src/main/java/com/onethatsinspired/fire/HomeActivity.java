package com.onethatsinspired.fire;

import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.onethatsinspired.fire.databinding.ActivityHomeBinding;

import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;


public class HomeActivity extends AppCompatActivity implements PodcastFragment.OnFragmentInteractionListener,YoutubeFragment.OnFragmentInteractionListener,
        BooksFragment.OnFragmentInteractionListener,ProsFragment.OnFragmentInteractionListener,BlogsFragment.OnFragmentInteractionListener
{

    private FirebaseFirestore db;

    private RecyclerAdapter adapter;

    private CollectionReference collectionReference;

    //ActivityHomeBinding activityHomeBinding;

    TabLayout tabLayout;

    HomePagerAdapter homeAdapter;

    MenuItem add_button;

    MenuItem settings_button;

    Menu mainMenu;

    CoordinatorLayout coordinatorLayout;

    @Override
    public  void onCreate(Bundle savedInstanceState)
     {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        //activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());

       // View view = activityHomeBinding.getRoot();

        //setContentView(view);

        Toolbar toolbarTop = findViewById(R.id.toolbarTop);

        setSupportActionBar(toolbarTop);

        configureTabLayout();

        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("podcast");

         tabLayout = findViewById(R.id.tabs);

         coordinatorLayout = findViewById(R.id.homeLayout);

     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        mainMenu = menu;

        add_button = mainMenu.getItem(0);

        settings_button = mainMenu.getItem(1);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);

        ImageView v = searchView.findViewById(searchImgId);

        v.setImageResource(R.drawable.sharp_search_white_18dp);

        //final PodcastFragment podcastFragment = (PodcastFragment)getSupportFragmentManager().getFragments().get(0);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {

            @Override
            public boolean onQueryTextSubmit(String query)
            {
               final Query searchQuery = collectionReference.whereEqualTo("name",query);

                switch (tabLayout.getSelectedTabPosition())
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

                searchQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {

                            switch (tabLayout.getSelectedTabPosition())
                            {
                                case 0:
                                    homeAdapter.podcastFragment.fireViewModelList.clear();
                                    homeAdapter.podcastFragment.adapter.notifyDataSetChanged();

                                   for (DocumentSnapshot documentSnapshot : task.getResult())
                                   {
                                       FireViewModel fireViewModel = new FireViewModel(
                                               documentSnapshot.getString("about"),
                                               documentSnapshot.getString("avgrating"),
                                               documentSnapshot.getString("link"),
                                               documentSnapshot.getString("name"),
                                               documentSnapshot.getString("type"));

                                       homeAdapter.podcastFragment.fireViewModelList.add(fireViewModel);
                                   }

                                   Log.e("Search", "Number of Podcast: " + String.valueOf(task.getResult().size()));

                                    adapter = new RecyclerAdapter(HomeActivity.this,homeAdapter.podcastFragment.fireViewModelList);

                                    homeAdapter.podcastFragment.recyclerView.setAdapter(adapter);

                                   break;
                                case 1:
                                    homeAdapter.youtubeFragment.fireViewModelList.clear();
                                    homeAdapter.youtubeFragment.adapter.notifyDataSetChanged();

                                    for (DocumentSnapshot documentSnapshot : task.getResult())
                                    {
                                        FireViewModel fireViewModel = new FireViewModel(
                                                documentSnapshot.getString("about"),
                                                documentSnapshot.getString("avgrating"),
                                                documentSnapshot.getString("link"),
                                                documentSnapshot.getString("name"),
                                                documentSnapshot.getString("type"));

                                        homeAdapter.youtubeFragment.fireViewModelList.add(fireViewModel);
                                    }

                                    Log.e("Search", "Number of Youtube Channels: " + String.valueOf(task.getResult().size()));

                                    adapter = new RecyclerAdapter(HomeActivity.this,homeAdapter.youtubeFragment.fireViewModelList);

                                    homeAdapter.youtubeFragment.recyclerView.setAdapter(adapter);
                                    break;
                                case 2:
                                    homeAdapter.booksFragment.fireViewModelList.clear();
                                    homeAdapter.booksFragment.adapter.notifyDataSetChanged();

                                    for (DocumentSnapshot documentSnapshot : task.getResult())
                                    {
                                        FireViewModel fireViewModel = new FireViewModel(
                                                documentSnapshot.getString("about"),
                                                documentSnapshot.getString("avgrating"),
                                                documentSnapshot.getString("link"),
                                                documentSnapshot.getString("name"),
                                                documentSnapshot.getString("type"));

                                        homeAdapter.booksFragment.fireViewModelList.add(fireViewModel);
                                    }

                                    Log.e("Search", "Number of Books: " + String.valueOf(task.getResult().size()));

                                    adapter = new RecyclerAdapter(HomeActivity.this,homeAdapter.booksFragment.fireViewModelList);

                                    homeAdapter.booksFragment.recyclerView.setAdapter(adapter);
                                    break;
                                case 3:
                                    homeAdapter.prosFragment.fireViewModelList.clear();
                                    homeAdapter.prosFragment.adapter.notifyDataSetChanged();

                                    for (DocumentSnapshot documentSnapshot : task.getResult())
                                    {
                                        FireViewModel fireViewModel = new FireViewModel(
                                                documentSnapshot.getString("about"),
                                                documentSnapshot.getString("avgrating"),
                                                documentSnapshot.getString("link"),
                                                documentSnapshot.getString("name"),
                                                documentSnapshot.getString("type"));

                                        homeAdapter.prosFragment.fireViewModelList.add(fireViewModel);
                                    }

                                    Log.e("Search", "Number of Pros: " + String.valueOf(task.getResult().size()));

                                    adapter = new RecyclerAdapter(HomeActivity.this,homeAdapter.prosFragment.fireViewModelList);

                                    homeAdapter.prosFragment.recyclerView.setAdapter(adapter);
                                    break;
                                case 4:
                                    homeAdapter.blogsFragment.fireViewModelList.clear();
                                    homeAdapter.blogsFragment.adapter.notifyDataSetChanged();

                                    for (DocumentSnapshot documentSnapshot : task.getResult())
                                    {
                                        FireViewModel fireViewModel = new FireViewModel(
                                                documentSnapshot.getString("about"),
                                                documentSnapshot.getString("avgrating"),
                                                documentSnapshot.getString("link"),
                                                documentSnapshot.getString("name"),
                                                documentSnapshot.getString("type"));

                                        homeAdapter.blogsFragment.fireViewModelList.add(fireViewModel);
                                    }

                                    Log.e("Search", "Number of Blogs: " + String.valueOf(task.getResult().size()));

                                    adapter = new RecyclerAdapter(HomeActivity.this,homeAdapter.blogsFragment.fireViewModelList);

                                    homeAdapter.blogsFragment.recyclerView.setAdapter(adapter);
                                    break;
                            }



                        Log.e("Search", "Search Success");

                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.e("Cant Search","Sorry");
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {

                return true;
            }

        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                switch (tabLayout.getSelectedTabPosition())
                {
                    case 0:
                        collectionReference = db.collection("podcast");
                        homeAdapter.podcastFragment.fireViewModelList.clear();
                        homeAdapter.podcastFragment.adapter.notifyDataSetChanged();
                        homeAdapter.podcastFragment.setUpData(collectionReference);
                        break;
                    case 1:
                        collectionReference = db.collection("youtube");
                        homeAdapter.youtubeFragment.fireViewModelList.clear();
                        homeAdapter.youtubeFragment.adapter.notifyDataSetChanged();
                        homeAdapter.youtubeFragment.setUpData(collectionReference);
                        break;
                    case 2:
                        collectionReference = db.collection("book");
                        homeAdapter.booksFragment.fireViewModelList.clear();
                        homeAdapter.booksFragment.adapter.notifyDataSetChanged();
                        homeAdapter.booksFragment.setUpData(collectionReference);
                        break;
                    case 3:
                        collectionReference = db.collection("pro");
                        homeAdapter.prosFragment.fireViewModelList.clear();
                        homeAdapter.prosFragment.adapter.notifyDataSetChanged();
                        homeAdapter.prosFragment.setUpData(collectionReference);
                        break;
                    case 4:
                        collectionReference = db.collection("blog");
                        homeAdapter.blogsFragment.fireViewModelList.clear();
                        homeAdapter.blogsFragment.adapter.notifyDataSetChanged();
                        homeAdapter.blogsFragment.setUpData(collectionReference);
                        break;
                }

                return false;
            }
        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.search:
                return true;
            case R.id.action_add:
                Intent i = new Intent(this,AddDataActivity.class);
                startActivity(i);
                return  true;
            case R.id.action_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public  void onFragmentInteraction(Uri uri)
    {

    }

    private void configureTabLayout()
    {
        tabLayout = findViewById(R.id.tabs);

        final ViewPager viewPager = findViewById(R.id.viewpager);

        homeAdapter = new HomePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        int numberOfTabs = tabLayout.getTabCount();

        viewPager.setAdapter(homeAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {

            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());

                //PodcastFragment podcastFragment = (PodcastFragment)getSupportFragmentManager().getFragments().get(tab.getPosition());

               // podcastFragment.resetFragment(tab.getPosition());
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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
               // PodcastFragment podcastFragment = (PodcastFragment)getSupportFragmentManager().getFragments().get(position);

               // podcastFragment.resetFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

    }

    public  void resetRecyclerAdapter(int itemPosition)
    {

        switch (tabLayout.getSelectedTabPosition())
        {
            case 0:
                homeAdapter.podcastFragment.adapter.notifyItemChanged(itemPosition);
                homeAdapter.podcastFragment.setUpData(homeAdapter.podcastFragment.collectionReference);
                homeAdapter.podcastFragment.adapter.notifyDataSetChanged();
                break;
            case 1:
                homeAdapter.youtubeFragment.adapter.notifyItemChanged(itemPosition);
                homeAdapter.youtubeFragment.setUpData(homeAdapter.youtubeFragment.collectionReference);
                homeAdapter.youtubeFragment.adapter.notifyDataSetChanged();
                break;
            case 2:
                homeAdapter.booksFragment.adapter.notifyItemChanged(itemPosition);
                homeAdapter.booksFragment.setUpData(homeAdapter.booksFragment.collectionReference);
                homeAdapter.booksFragment.adapter.notifyDataSetChanged();
                break;
            case 3:
                homeAdapter.prosFragment.adapter.notifyItemChanged(itemPosition);
                homeAdapter.prosFragment.setUpData(homeAdapter.prosFragment.collectionReference);
                homeAdapter.prosFragment.adapter.notifyDataSetChanged();
                break;
            case 4:
                homeAdapter.blogsFragment.adapter.notifyItemChanged(itemPosition);
                homeAdapter.blogsFragment.setUpData(homeAdapter.blogsFragment.collectionReference);
                homeAdapter.blogsFragment.adapter.notifyDataSetChanged();
                break;
        }


        //adapter.notifyDataSetChanged();
    }
}
