package com.onethatsinspired.fire.adapters;

import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.onethatsinspired.fire.controllers.activities.HomeActivity;
import com.onethatsinspired.fire.controllers.views.ItemHolder;
import com.onethatsinspired.fire.R;
import com.onethatsinspired.fire.viewmodels.FireViewModel;

import java.util.List;



public class FirebaseRecyclerAdapter extends FirestoreRecyclerAdapter<FireViewModel,ItemHolder>
{


    HomeActivity homeActivity;

    List<FireViewModel> fireViewModelList;

    /**** View Holder Functions ****/

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_itemcard, parent,false);

        return new ItemHolder(v);
    }


    public FirebaseRecyclerAdapter(@NonNull FirestoreRecyclerOptions<FireViewModel> options)
    {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull ItemHolder itemHolder, int i, @NonNull FireViewModel fireViewModel)
    {
        itemHolder.textViewName.setText(fireViewModel.getName());

        itemHolder.ratingBarAverage.setRating(Integer.parseInt(fireViewModel.getAvgRating()));

        itemHolder.ultraName = fireViewModel.getName();

        itemHolder.ultraRating = fireViewModel.getAvgRating();

        itemHolder.ultraLink = fireViewModel.getLink();

        itemHolder.ultraAbout = fireViewModel.getAbout();

        itemHolder.ultraType = fireViewModel.getType();

        itemHolder.ultraId = fireViewModel.getType();

    }


}