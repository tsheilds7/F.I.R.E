package com.onethatsinspired.fire.controllers.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.onethatsinspired.fire.controllers.activities.HomeActivity;
import com.onethatsinspired.fire.R;
import com.onethatsinspired.fire.adapters.RecyclerAdapter;
import com.onethatsinspired.fire.databinding.FragmentYoutubeBinding;
import com.onethatsinspired.fire.repositories.FIreRepo;
import com.onethatsinspired.fire.viewmodels.FireViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.onethatsinspired.fire.BR.FireVMPodcasts;


/**
 * A simple {@link Fragment} subclass.
 */
public class YoutubeFragment extends Fragment
{

    private FragmentYoutubeBinding fragmentYoutubeBinding;

    public RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    public RecyclerAdapter adapter;

    public List<FireViewModel> fireViewModelList = new ArrayList<>();

    public FIreRepo fIreRepo;

    private  FireViewModel fireViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentYoutubeBinding = FragmentYoutubeBinding.inflate(inflater,container,false);

        fragmentYoutubeBinding.setLifecycleOwner(this);

        View view = fragmentYoutubeBinding.getRoot();

        fIreRepo = new FIreRepo();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        fireViewModel =  ViewModelProviders.of(this).get(FireViewModel.class);

        fragmentYoutubeBinding.setVariable(FireVMPodcasts,fireViewModel);

        recyclerView = getActivity().findViewById(R.id.recyclerViewYoutube);

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);


        setUpRecycler(fIreRepo.setUpData(1));
    }



    public void setUpRecycler(FirestoreRecyclerOptions<FireViewModel> options)
    {
        adapter = new RecyclerAdapter(options);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }

}