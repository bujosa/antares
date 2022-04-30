package com.bujosa.antares.services;

import android.annotation.SuppressLint;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

@SuppressWarnings("ALL")
public class FirestoreService {
    private static String userId;
    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore mDatabase;
    private static FirestoreService service;

    public static FirestoreService getServiceInstance(){
        if( service == null || mDatabase == null){
            mDatabase = FirebaseFirestore.getInstance();
            service = new FirestoreService();
        }
        if(userId == null || userId.isEmpty()){
            userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                    FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
        }
        return service;
    }
}
