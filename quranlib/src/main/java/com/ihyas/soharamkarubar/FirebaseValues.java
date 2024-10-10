package com.ihyas.soharamkarubar;

/*import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;*/

public class FirebaseValues {




/*
    public static void getAndSaveValuesFromFirebase(Context context){
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("Islam786").document("Controls");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Constants.AdsSwitch = document.getString("AdsSwitch");
                        Constants.admobBannerId = document.getString("AdmobBannerID");
                        Constants.admobInter = document.getString("AdmobInterID");
                        Constants.fbBannerId = document.getString("FbBannerID");

                        // launch next activity
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
*/

/*    public static void getValuesfromFirebase(Context context){
        FirebaseDatabase database;
        DatabaseReference databaseReference;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Controls");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    Constants.AdsSwitch = snapshot.child("AdsSwitch").getValue().toString();
                    Constants.fbBannerId = snapshot.child("AdmobBannerID").getValue().toString();
                    Constants.admobBannerId = snapshot.child("AdmobInterID").getValue().toString();
                    Constants.admobInter = snapshot.child("FbBannerID").getValue().toString();
                }
                catch (Exception e){
                    Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/

}
