package com.areeb.supertextiles.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;

public interface SearchInCustomer {
    void onSearchInCustomer(Query firebaseSearchQuery, RecyclerView customersRecyclerView);
}
