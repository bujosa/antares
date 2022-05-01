package com.bujosa.antares.travel;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.annotation.Nullable;

public class TravelService {

    final private FirebaseFirestore db;
    final private FirebaseStorage storage;

    public TravelService(FirebaseFirestore db, FirebaseStorage storage) {
        this.db = db;
        this.storage = storage;
    }

    public Task<QuerySnapshot> loadTravels() {
        Query query = db.collection("travels")
                .orderBy("startDate", Query.Direction.DESCENDING);
        return query.get();
    }

    public Task<DocumentReference> addTravel(Travel travel) {
        return db.collection("travels").add(travel);
    }

    public Task<Void> deleteTravel(Travel travel) {
        return db.collection("travels").document(travel.getId())
                .delete();
    }

    public UploadTask addTravelPicture(Uri pictureFile) {
        StorageReference storageRef = storage.getReference()
                .child("images/" + pictureFile.getLastPathSegment());
        return storageRef.putFile(pictureFile);
    }
}
