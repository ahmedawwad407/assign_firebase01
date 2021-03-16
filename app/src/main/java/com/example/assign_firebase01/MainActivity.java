package com.example.assign_firebase01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editTextName;
    EditText editTextNumber;
    EditText editTextAddress;
    ListView myList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.name);
        editTextNumber = findViewById(R.id.number);
        editTextAddress = findViewById(R.id.address);
        myList = findViewById(R.id.ListView);

    }


    public void saveToFirbase(View v) {

        String name = editTextName.getText().toString();
        String number = editTextNumber.getText().toString();
        String address = editTextAddress.getText().toString();

        Map<String, Object> contactDetails = new HashMap<>();
        contactDetails.put("Name", name);
        contactDetails.put("number", number);
        contactDetails.put("Address", address);
        db.collection("contacts")
                .add(contactDetails)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();

                    }
                });
    }

    public void getDataFromFirestore(View v) {

        db.collection("contacts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "isEmpty", Toast.LENGTH_LONG).show();
                            return;

                        } else {
                                for (int x = 0; x <= documentSnapshots.size() - 1; x++) {
                                    String details = documentSnapshots.getDocuments().get(x).getData().toString();
                                    String id = documentSnapshots.getDocuments().get(x).getId();

                                    list.add(id +" => "+details);
                                    System.out.println(list);
                                    Log.d("a", "ContactId: " + id + " => " + "contactDetails: " + details + "\n");
                                }
//
                          ArrayAdapter  arrayAdapter =new ArrayAdapter(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item, list);

                            myList.setAdapter(arrayAdapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
    }

}