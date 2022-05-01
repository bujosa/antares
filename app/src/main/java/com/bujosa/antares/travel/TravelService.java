package com.bujosa.antares.travel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class TravelService {

    final private FirebaseFirestore database;

    public TravelService(FirebaseFirestore database) {
        this.database = database;
    }

    public Task<QuerySnapshot> loadTravels(@Nullable FilterInputs filterInputs) {

        Query query = database.collection("travels");

        if (filterInputs != null) {
            if (filterInputs.hasFavorite()) {
                query = query.whereEqualTo("favorite", filterInputs.getFavorite());
            }
        }

        return query.get();
    }

    public Task<DocumentReference> addTravel(Travel travel) {
        return database.collection("travels").add(travel);
    }

    public Task<Void> deleteTravel(Travel travel) {
        return database.collection("travels")
                .document(travel.getId())
                .delete();
    }

    public void updateTravel(Travel travel) {
        database.collection("travels")
                .document(travel.getId())
                .update("favorite", travel.getFavorite());
    }

}
