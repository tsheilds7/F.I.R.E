package com.onethatsinspired.fire;

import android.content.Context;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onethatsinspired.fire.viewmodels.FireViewModel;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ItemHolder>
{

    Context context;

    HomeActivity homeActivity;

    List<FireViewModel> fireViewModelList;

    public RecyclerAdapter(HomeActivity homeActivity, List<FireViewModel> fireviewModelList)
    {
        this.homeActivity = homeActivity;

        this.fireViewModelList = fireviewModelList;
    }



    /**** View Holder Functions ****/

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_itemcard, parent,false);

        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position)
    {
        holder.textViewName.setText(fireViewModelList.get(position).getName());

        holder.ratingBarAverage.setRating(Integer.parseInt(fireViewModelList.get(position).getAvgRating()));

        holder.ultraName = fireViewModelList.get(position).getName();

        holder.ultraRating = fireViewModelList.get(position).getAvgRating();

        holder.ultraLink = fireViewModelList.get(position).getLink();

        holder.ultraAbout = fireViewModelList.get(position).getAbout();

        holder.ultraType = fireViewModelList.get(position).getType();

        holder.ultraId = fireViewModelList.get(position).getType();
    }

    @Override
    public int getItemCount()
    {
        return fireViewModelList.size();
    }



    public  void reset()
    {
        notifyDataSetChanged();
    }



}




