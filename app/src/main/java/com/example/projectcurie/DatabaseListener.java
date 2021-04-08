package com.example.projectcurie;

import com.google.firebase.firestore.QuerySnapshot;

/**
 * This interface must be implemented by any object wishing to be notified of a query result or
 * data changes from the DatabaseController.
 * @author Joshua Billson
 */
public interface DatabaseListener {

    /**
     * This method is called by the DatabaseController to notify the listener that a query has
     * returned or that a watcher detected a change in some underlying data.
     * @param data
     *     The data returned from the FireStore database that this listener is interested in.
     * @param returnCode
     *     A unique code used to identify an observer if the listener is interested in multiple
     *     data-sets.
     */
    public void notifyDataChanged(QuerySnapshot data, int returnCode);
}
