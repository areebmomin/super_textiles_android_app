package com.areeb.supertextiles.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;

public interface SearchInReport {
    void onSearchInReport(Query firebaseSearchQuery, RecyclerView reportRecyclerView);
}
