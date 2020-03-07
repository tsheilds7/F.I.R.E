package com.onethatsinspired.fire;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.onethatsinspired.fire.databinding.FragmentBooksBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment
{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("book");

    private FragmentBooksBinding fragmentBooksBinding;

    Query query =  collectionReference.orderBy("avgrating",Query.Direction.DESCENDING);

    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    RecyclerAdapter adapter;

    List<FireViewModel> fireViewModelList = new ArrayList<>();


    public BooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       fragmentBooksBinding = FragmentBooksBinding.inflate(inflater,container,false);
       View view = fragmentBooksBinding.getRoot();
       return  view;
    }




    public void setUpData()
    {

        if((!fireViewModelList.isEmpty()) || (adapter != null))
        {
            fireViewModelList.clear();
            adapter.notifyDataSetChanged();
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                for(DocumentSnapshot documentSnapshot : task.getResult())
                {
                    FireViewModel fireViewModel = new FireViewModel(
                            documentSnapshot.getString("about"),
                            documentSnapshot.getString("avgrating"),
                            documentSnapshot.getString("link"),
                            documentSnapshot.getString("name"),
                            documentSnapshot.getString("type"));

                    fireViewModelList.add(fireViewModel);
                }

                adapter = new RecyclerAdapter((HomeActivity)getActivity(),fireViewModelList);

                recyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {

            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        recyclerView = getActivity().findViewById(R.id.recyclerViewBooks);

        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        setUpData();
    }

    @Override
    public void onStart()
    {

        super.onStart();
        //adapter.startListening();

    }

    @Override
    public void onStop()
    {
        super.onStop();
        //adapter.stopListening();
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }

}