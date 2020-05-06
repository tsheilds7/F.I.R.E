package com.onethatsinspired.fire.controllers.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.onethatsinspired.fire.R;
import com.onethatsinspired.fire.adapters.FirebaseRecyclerAdapter;
import com.onethatsinspired.fire.databinding.FragmentPodcastBinding;
import com.onethatsinspired.fire.repositories.FIreRepo;
import com.onethatsinspired.fire.viewmodels.FireViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.onethatsinspired.fire.BR.FireVMPodcasts;
import static com.onethatsinspired.fire.BR.FireVMPodcasts;

/**
 * A simple {@link Fragment} subclass.
 */
public class PodcastFragment extends Fragment
{

    private FragmentPodcastBinding fragmentPodcastBinding;

    public RecyclerView recyclerView;

    private  FireViewModel fireViewModel;

    public List<FireViewModel> fireViewModelList = new ArrayList<>();

    RecyclerView.LayoutManager layoutManager;

    public FirebaseRecyclerAdapter adapter;

    public FIreRepo fIreRepo;

    public PodcastFragment()
    {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentPodcastBinding = FragmentPodcastBinding.inflate(inflater, container, false);

        fragmentPodcastBinding.setLifecycleOwner(this);

        View view = fragmentPodcastBinding.getRoot();

        fIreRepo = new FIreRepo();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        fireViewModel =  ViewModelProviders.of(this).get(FireViewModel.class);

        fragmentPodcastBinding.setVariable(FireVMPodcasts,fireViewModel);

        recyclerView = getActivity().findViewById(R.id.recyclerViewPodcast);

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        setUpRecycler(fIreRepo.setUpData(0));
    }

    public void setUpRecycler(FirestoreRecyclerOptions<FireViewModel> options)
    {
        adapter = new FirebaseRecyclerAdapter(options);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public void resetAdapter()
    {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public  void onCreate(Bundle savedBundleInstance)
    {
        super.onCreate(savedBundleInstance);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public  void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }

}