package com.onethatsinspired.fire.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.firebase.firestore.FirebaseFirestore;
import com.onethatsinspired.fire.controllers.fragments.BlogsFragment;
import com.onethatsinspired.fire.controllers.fragments.BooksFragment;
import com.onethatsinspired.fire.controllers.fragments.PodcastFragment;
import com.onethatsinspired.fire.controllers.fragments.ProsFragment;
import com.onethatsinspired.fire.controllers.fragments.YoutubeFragment;

public class HomePagerAdapter extends FragmentPagerAdapter
{
    int tabCount;

    public PodcastFragment podcastFragment;
    public YoutubeFragment youtubeFragment;
    public BooksFragment booksFragment;
    public ProsFragment prosFragment;
    public BlogsFragment blogsFragment;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HomePagerAdapter(FragmentManager fm, int numberOfTabs)
    {
        super(fm);

        this.tabCount = numberOfTabs;
    }


    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                podcastFragment = new PodcastFragment();
                return podcastFragment;
            case 1:
                youtubeFragment = new YoutubeFragment();
                return youtubeFragment;
            case 2:
                booksFragment = new BooksFragment();
                return booksFragment;
            case 3:
                prosFragment = new ProsFragment();
                return prosFragment;
            case 4:
                blogsFragment = new BlogsFragment();
                return blogsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return tabCount;
    }
}