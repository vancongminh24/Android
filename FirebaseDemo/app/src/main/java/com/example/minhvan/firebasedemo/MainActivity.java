package com.example.minhvan.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private EditText inputName, inputEmail;
    private ListView listView;
    private ProgressBar progressBar;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<User> userArrayList;
    private CustomListViewAdapter customListViewAdapter;
    private User selectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Firebase Demo");
        setSupportActionBar(toolbar);

        //control
        progressBar = (ProgressBar)findViewById(R.id.circularProgress);
        inputEmail = (EditText) findViewById(R.id.email);
        inputName = (EditText)findViewById(R.id.name);
        listView = (ListView)findViewById((R.id.listView));
        userArrayList = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User)adapterView.getItemAtPosition(i);
                selectedUser = user;
                Log.d("selectedUser", "" +user.getUid() + " " +selectedUser.getEmail());
                inputName.setText(user.getName());
                inputEmail.setText(user.getEmail());
            }
        });
        //Firebase
        initFirebase();
        addEventFirebaseListener();
    }

    private void addEventFirebaseListener() {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        //this will listen to any changes to our database under child "users"
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //make sure the userArrayList is empty before copying
                if(userArrayList.size()>0){
                    userArrayList.clear();
                }
                //copy each value into userArrayList
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    User user = postSnapshot.getValue(User.class);
                    userArrayList.add(user);
                    Log.d("userArrayList", "" + user.getUid() + " " + user.getEmail());
                }
                //then pass userArrayList into CustomListViewAdapter
                customListViewAdapter = new CustomListViewAdapter(MainActivity.this, userArrayList);
                //then setAdapter. The list will be shown in UI
                listView.setAdapter(customListViewAdapter);
                //set Visibility to indicate that the works is done
                progressBar.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_add){
            createUser();
        }else if(item.getItemId() == R.id.menu_save){
            User user = new User(selectedUser.getUid(), inputName.getText().toString(), inputEmail.getText().toString());
            updateUser(user);
        }else if(item.getItemId() == R.id.menu_remove){
            deleteUser(selectedUser);
        }
        return true;
    }

    private void deleteUser(User selectedUser) {
        databaseReference.child("users").child(selectedUser.getUid()).removeValue();
        clearEditText();
    }

    private void updateUser(User user) {
        databaseReference.child("users").child(user.getUid()).child("name").setValue(user.getName());
        databaseReference.child("users").child(user.getUid()).child("email").setValue(user.getEmail());
        clearEditText();
    }

    private void createUser() {
        //create instance user with following 3 properties
        User user = new User(UUID.randomUUID().toString(), inputName.getText().toString(),inputEmail.getText().toString());
        //create a path. and The key of database is the 3 available according getter() in the instance class.
        databaseReference.child("users").child(user.getUid()).setValue(user);
        clearEditText();
    }

    private void clearEditText() {
        inputName.setText("");
        inputEmail.setText("");
    }
}
