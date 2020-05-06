package com.onethatsinspired.fire.controllers.activities;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import com.onethatsinspired.fire.R;

import com.onethatsinspired.fire.adapters.HomePagerAdapter;

import com.onethatsinspired.fire.adapters.FirebaseRecyclerAdapter;

import com.onethatsinspired.fire.controllers.fragments.BlogsFragment;
import com.onethatsinspired.fire.controllers.fragments.BooksFragment;
import com.onethatsinspired.fire.controllers.fragments.PodcastFragment;
import com.onethatsinspired.fire.controllers.fragments.ProsFragment;
import com.onethatsinspired.fire.controllers.fragments.YoutubeFragment;
import com.onethatsinspired.fire.repositories.FIreRepo;


public class HomeActivity extends AppCompatActivity implements PodcastFragment.OnFragmentInteractionListener, YoutubeFragment.OnFragmentInteractionListener,
        BooksFragment.OnFragmentInteractionListener, ProsFragment.OnFragmentInteractionListener, BlogsFragment.OnFragmentInteractionListener
{

    private FirebaseFirestore db;

    private FirebaseRecyclerAdapter adapter;

    private CollectionReference collectionReference;

    public TabLayout tabLayout;

    HomePagerAdapter homeAdapter;

    public MenuItem add_button;

    public MenuItem settings_button;

    Menu mainMenu;

    public ConstraintLayout coordinatorLayout;

    Client client;

    public Index index;

    @Override
    public  void onCreate(Bundle savedInstanceState)
     {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        Toolbar toolbarTop = findViewById(R.id.toolbarTop);

        setSupportActionBar(toolbarTop);

        configureTabLayout();

        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("podcast");

         tabLayout = findViewById(R.id.tabs);

         coordinatorLayout = findViewById(R.id.homeLayout);

         client = new Client("4FW4PHPOL5", "709954ad213a5f950da58271d7542581");

         index = client.getIndex("podcast");


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

        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //searchView.setIconifiedByDefault(false);

        searchView.setSubmitButtonEnabled(false);

        int searchImgId = getResources().getIdentifier("android:id/search_button", null, null);

      //  ImageView searchButtonImage = searchView.findViewById(searchImgId);

       // searchButtonImage.setImageResource(R.drawable.sharp_search_white_18dp);

       // ImageView closeButtonImage = searchView.findViewById(R.id.search_close_btn);

        //closeButtonImage.setImageResource(R.drawable.sharp_search_white_18dp);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener()
        {

            FIreRepo fIreRepo = new FIreRepo();

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                fIreRepo.performAlgoliaSearch(tabLayout.getSelectedTabPosition(),index,query,homeAdapter);

                return true;
            }

        });



        searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {

                switch(tabLayout.getSelectedTabPosition())
                {
                    case 0:
                        homeAdapter.podcastFragment.resetAdapter();
                        break;
                    case 1:
                        homeAdapter.youtubeFragment.resetAdapter();
                        break;
                    case 2:
                        homeAdapter.booksFragment.resetAdapter();
                        break;
                    case 3:
                        homeAdapter.prosFragment.resetAdapter();
                        break;
                    case 4:
                        homeAdapter.blogsFragment.resetAdapter();
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


                switch (tab.getPosition())
                {
                    case 0:
                        index = client.getIndex("podcast");
                        break;
                    case 1:
                        index = client.getIndex("youtube");
                        break;
                    case 2:
                        index = client.getIndex("book");
                        break;
                    case 3:
                        index = client.getIndex("pro");
                        break;
                    case 4:
                        index = client.getIndex("blog");
                        break;
                    default:
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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

    }

}