package com.areeb.supertextiles.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;

public interface SearchInBills {
    void onSearchInBills(Query firebaseSearchQuery, RecyclerView billsRecyclerView);
}
