package com.onethatsinspired.fire.controllers.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.onethatsinspired.fire.R;
import com.onethatsinspired.fire.adapters.FirebaseRecyclerAdapter;
import com.onethatsinspired.fire.databinding.FragmentBooksBinding;
import com.onethatsinspired.fire.repositories.FIreRepo;
import com.onethatsinspired.fire.viewmodels.FireViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.onethatsinspired.fire.BR.FireVMPodcasts;


/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment
{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CollectionReference collectionReference = db.collection("book");

    private FragmentBooksBinding fragmentBooksBinding;

    private LifecycleRegistry lifecycleRegistry;

    Query query =  collectionReference.orderBy("avgrating",Query.Direction.DESCENDING);

    public RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    public FirebaseRecyclerAdapter adapter;

    public List<FireViewModel> fireViewModelList = new ArrayList<>();

    public FIreRepo fIreRepo;

    private  FireViewModel fireViewModel;




    public BooksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       fragmentBooksBinding = FragmentBooksBinding.inflate(inflater,container,false);

       fragmentBooksBinding.setLifecycleOwner(this);

       View view = fragmentBooksBinding.getRoot();

       fIreRepo = new FIreRepo();

       return  view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        fireViewModel =  ViewModelProviders.of(this).get(FireViewModel.class);

        fragmentBooksBinding.setVariable(FireVMPodcasts,fireViewModel);

        recyclerView = getActivity().findViewById(R.id.recyclerViewBooks);

        layoutManager = new LinearLayoutManager(getContext());


        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);

        setUpRecycler(fIreRepo.setUpData(2));
    }

    @Override
    public void onCreate(Bundle savedBundleInstance)
    {
        super.onCreate(savedBundleInstance);
    }

    public void setUpRecycler(FirestoreRecyclerOptions<FireViewModel> options)
    {
        adapter = new FirebaseRecyclerAdapter(options);

        recyclerView.setAdapter(adapter);
    }

    public void resetAdapter()
    {
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