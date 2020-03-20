package com.onethatsinspired.fire.controllers.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
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
import com.onethatsinspired.fire.databinding.FragmentProsBinding;
import com.onethatsinspired.fire.repositories.FIreRepo;
import com.onethatsinspired.fire.viewmodels.FireViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.onethatsinspired.fire.BR.FireVMPodcasts;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProsFragment extends Fragment
{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public  CollectionReference collectionReference = db.collection("pro");

    private FragmentProsBinding fragmentProsBinding;

    private LifecycleRegistry lifecycleRegistry;

    Query query =  collectionReference.orderBy("avgrating",Query.Direction.DESCENDING);

    public RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    public RecyclerAdapter adapter;

    public List<FireViewModel> fireViewModelList = new ArrayList<>();

    public FIreRepo fIreRepo;

    private  FireViewModel fireViewModel;



    public ProsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       fragmentProsBinding = FragmentProsBinding.inflate(inflater,container,false);

       fragmentProsBinding.setLifecycleOwner(this);

       View view = fragmentProsBinding.getRoot();

       fIreRepo = new FIreRepo();

       return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        fireViewModel =  ViewModelProviders.of(this).get(FireViewModel.class);

        fragmentProsBinding.setVariable(FireVMPodcasts,fireViewModel);

        recyclerView = getActivity().findViewById(R.id.recyclerViewPros);

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        setUpRecycler(fIreRepo.setUpData(3));
    }

    @Override
    public void onCreate(Bundle savedBundleInstance)
    {
        super.onCreate(savedBundleInstance);
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