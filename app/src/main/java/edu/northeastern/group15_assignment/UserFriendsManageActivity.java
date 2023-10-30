package edu.northeastern.group15_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserFriendsManageActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    RecyclerView friendListRecyclerView;

    static List<String> allFriendsLocal;

    String currentUsername;

    private boolean validateUsername(String username){
        // No empty string
        // No null
        // Must be longer than 3 characters and shorter than 20 characters
        if (username == null || username.length() < 3 || username.length() > 20) {
            return false;
        }

        // No spaces
        for (int i = 0; i < username.length(); i++) {
            if (username.charAt(i) == ' ') {
                return false;
            }
        }

        // No special characters
        if (!username.matches("[a-zA-Z0-9]*")) {
            return false;
        }

        return true;
    }

    private DataSnapshot getValueFromDataSnapshot(Task<DataSnapshot> task) {
        // wait for the task to complete
        while (!task.isComplete()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return task.getResult();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_friends_manage);

        currentUsername = getIntent().getStringExtra("username");

        System.out.println("CURRENT USERNAME FRIENDS PAGE:" + currentUsername);

        rootNode = FirebaseDatabase.getInstance("https://a8-group15-new-default-rtdb.firebaseio.com/");

        // Initialize the friend list recycler view
        friendListRecyclerView = findViewById(R.id.user_friends_manage_recycler_view);

        // Set the layout manager
        friendListRecyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        // Retrieve the current user's friends list
        DatabaseReference currentUserFriendsList = rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/").getRef()
                .child("username")
                .child(currentUsername)
                .child("friends");

        // Get the values from the database reference
        allFriendsLocal = (List<String>) getValueFromDataSnapshot(currentUserFriendsList.get()).getValue();

        if (allFriendsLocal == null) {
            allFriendsLocal = new ArrayList<>();
        }

        System.out.println("FRIENDS LIST:" + allFriendsLocal);

        // Set the adapter
        friendListRecyclerView.setAdapter(new FriendAdapter(allFriendsLocal, this));

        // Set listeners for the add button
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.user_friends_manage_add);
        addButton.setOnClickListener(v -> {
            // Get the value of the textview with id username
            String username = ((EditText) findViewById(R.id.user_friends_manage_search)).getText().toString();

            List<String> allUsers = new ArrayList<>();

            // Validate the username
            DataSnapshot lastDataSnapShot = null;
            Iterable<DataSnapshot> iterable = getValueFromDataSnapshot(rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/").getRef().child("username").get()).getChildren();
            for (Iterator iterator = iterable.iterator(); iterator.hasNext();) {
                lastDataSnapShot = (DataSnapshot) iterator.next();
                allUsers.add(lastDataSnapShot.getKey().toString());
            }
            System.out.println("ALL USERS:" + allUsers);


            if (!allUsers.contains(username)) {
                // If the username is not present, display a snackbar with the message "Username not found"
                Toast.makeText(getApplicationContext(), "User of name : " + username + " does not exist", Toast.LENGTH_LONG).show();
                return;
            }
            else {

                // Retrieve the current user's friends list
                DatabaseReference currentUserFriends = rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/").getRef()
                        .child("username")
                        .child(currentUsername)
                        .child("friends");

                // Add the new friend to the current user's friends list
                // Get the whole list
                List<String> friendsList = (List<String>) getValueFromDataSnapshot(currentUserFriends.get()).getValue();
                if (friendsList.contains(username)) {
                    // If the username is present, display a snackbar with the message "Login successful"
                    Toast.makeText(getApplicationContext(), "User is already a friend", Toast.LENGTH_LONG).show();
                    return;
                }

                friendsList.add(username);
                currentUserFriends.setValue(friendsList);
                this.allFriendsLocal.add(username);
                // Notify the adapter that the data has changed
                friendListRecyclerView.getAdapter().notifyDataSetChanged();

                // If the username is present, display a snackbar with the message "Login successful"
                Toast.makeText(getApplicationContext(), "Add friend successful", Toast.LENGTH_LONG).show();
                System.out.println("UPDATES : " + friendListRecyclerView.hasPendingAdapterUpdates());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("username", this.currentUsername);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.currentUsername = savedInstanceState.getString("username");
    }
}