package com.areeb.supertextiles.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;

public interface SearchInChallan {
    void onSearchInChallan(Query firebaseSearchQuery, RecyclerView challansRecyclerView);
}
