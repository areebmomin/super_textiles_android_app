package com.areeb.supertextiles;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChallansFragment extends Fragment {

    public ChallansFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialize fragment view
        View fragmentView = inflater.inflate(R.layout.fragment_challans, container, false);

        //initialize xml view
        FloatingActionButton addChallanButton = fragmentView.findViewById(R.id.addChallanButton);


        //addChallanButton onClickListener
        addChallanButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddChallanActivity.class);
            startActivity(intent);
        });

        // Inflate the layout for this fragment
        return fragmentView;
    }
}