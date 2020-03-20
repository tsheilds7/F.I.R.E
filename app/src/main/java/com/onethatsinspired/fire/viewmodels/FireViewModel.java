package com.onethatsinspired.fire.viewmodels;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import javax.annotation.Nullable;

public class FireViewModel extends ViewModel
{
    private  String about;

    private String avgrating;

    private  String link;

    private  String name;

    private  String type;

    public  FireViewModel()
    {

    }

    public FireViewModel(String about, String avgrating, String link, String name, String type)
    {
        this.about = about;

        this.avgrating = avgrating;

        this.link = link;

        this.name = name;

        this.type = type;
    }

    public String getAbout() { return about; }

    public String getAvgRating() { return  avgrating; }

    public String getLink() { return link; }

    public String getName()
    {
        return name;
    }

    public String getType() { return type; }


}