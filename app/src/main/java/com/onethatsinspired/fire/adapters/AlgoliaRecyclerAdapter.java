package com.onethatsinspired.fire.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onethatsinspired.fire.R;
import com.onethatsinspired.fire.controllers.views.ItemHolder;
import com.onethatsinspired.fire.viewmodels.FireViewModel;

import java.util.List;

public class AlgoliaRecyclerAdapter extends RecyclerView.Adapter<ItemHolder>
{

    List<FireViewModel> fireViewModelList;

    Context context;

    public AlgoliaRecyclerAdapter(Context context, List<FireViewModel> fireViewModelList)
    {
       this.context = context;

       this.fireViewModelList = fireViewModelList;
    }

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
        return  fireViewModelList.size();
    }
}
