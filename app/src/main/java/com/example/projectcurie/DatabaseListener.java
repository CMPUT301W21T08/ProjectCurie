package com.example.projectcurie;

import com.google.firebase.firestore.QuerySnapshot;

public interface DatabaseListener {
    public void notifyDataChanged(QuerySnapshot data, int returnCode);
}
