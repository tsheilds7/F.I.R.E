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

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupWindow;

import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

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




