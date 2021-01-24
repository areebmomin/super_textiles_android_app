package com.areeb.supertextiles.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.areeb.supertextiles.activities.QualitiesActivity;
import com.areeb.supertextiles.R;
import com.google.android.material.card.MaterialCardView;

public class MoreFragment extends Fragment {

    MaterialCardView qualityCardView;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        //initialize xml views
        qualityCardView = view.findViewById(R.id.qualityCardView);

        //quality card view onClick method
        qualityCardView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), QualitiesActivity.class);
            startActivity(intent);
        });

        return view;
    }
}