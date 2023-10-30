package edu.northeastern.group15_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StickItLoginActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_login);

        // Initialize FirebaseApp
        FirebaseApp.initializeApp(this);

        // Initialize FirebaseDatabase
        rootNode = FirebaseDatabase.getInstance();
    }

    public void onLoginClick(View view){

        // Get the value of the textview with id username
        String username = ((TextView) findViewById(R.id.username_tv)).getText().toString();

        // Validate the presence of a username in the username list of the database
        if (
                validateUsernameInDatabase(username)
        ) {
            // If the username is present, display a snackbar with the message "Login successful"
            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
        } else {
            // If the username is not present, display a snackbar with the message "Username not found"
            Toast.makeText(getApplicationContext(), "Username not found", Toast.LENGTH_LONG).show();
            return;
        }

        // If the username is present, display a snackbar with the message "Login successful"
        // and start the StickItActivity

        Intent intent = new Intent(this, StickItActivity.class);
        intent.putExtra("username", username);

        System.out.println("CURRENT USERNAME LOGIN PAGE:" + username);

        startActivity(intent);
    }

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

    private boolean validateUsernameInDatabase(String username) {
        return getValueFromDataSnapshot(rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/").getRef().child("username").child(username).get()).getValue() != null;
    }

    public void onRegisterClick(View view){
        // Validate the username
        String username = ((TextView) findViewById(R.id.username_tv)).getText().toString();

        System.out.println("DBG USERNAME:" + username);

        if (!validateUsername(username)) {
            // If the username is invalid, display a snackbar with the message "Invalid username"
            Toast.makeText(getApplicationContext(), "Invalid username", Toast.LENGTH_LONG).show();
            return;
        }
        System.out.println("DBG DATABASE REF");
        Object username_present = getValueFromDataSnapshot(rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/").getRef().child("username").child(username).get()).getValue();

        // Check if a user is already registered with the said username
        if (username_present != null) {
            // If a user is already registered with the said username, display a snackbar with the message "Username already taken"
            Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_LONG).show();
            return;
        }

        // If the username is valid, add the username to the username list of the database
        // and display a snackbar with the message "Registration successful"
        rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/").getRef()
                .child("username")
                .child(username)
                .child("exists")
                .setValue(true)
                .addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                // FAILURE
                // Make a toast
                Toast.makeText(getApplicationContext(), "Failed to register user. Please try again", Toast.LENGTH_LONG).show();
                System.out.println("ERROR:" + task.getException().getMessage());
            }
            else {
                Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                System.out.println("SUCCESS:" + task.getResult());
            }
        });

        // Add a friends key to the user's entry in the database
        rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/").getRef()
                .child("username")
                .child(username)
                .child("friends")
                .setValue(List.of(""))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // FAILURE
                        // Make a toast
                        Toast.makeText(getApplicationContext(), "Failed to register user. Please try again", Toast.LENGTH_LONG).show();
                        System.out.println("ERROR:" + task.getException().getMessage());
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                        System.out.println("SUCCESS:" + task.getResult());
                    }
                });

        // Add a stickers key to the user's entry in the database
        rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/").getRef()
                .child("username")
                .child(username)
                .child("stickers_received")
                .setValue(List.of(""))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // FAILURE
                        // Make a toast
                        Toast.makeText(getApplicationContext(), "Failed to register user. Please try again", Toast.LENGTH_LONG).show();
                        System.out.println("ERROR:" + task.getException().getMessage());
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                        System.out.println("SUCCESS:" + task.getResult());
                    }
                });

        // Add map of sticker counts to the user's entry in the database
        Map<String, Long> blankCounts = new HashMap<String, Long>();
        blankCounts.put("HAPPY", Integer.toUnsignedLong( 0));
        blankCounts.put("SAD", Integer.toUnsignedLong(0));
        blankCounts.put("COOL", Integer.toUnsignedLong(0));
        rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/").getRef()
                .child("username")
                .child(username)
                .child("sticker_counts")
                .setValue(blankCounts)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // FAILURE
                        // Make a toast
                        Toast.makeText(getApplicationContext(), "Failed to register user. Please try again", Toast.LENGTH_LONG).show();
                        System.out.println("ERROR:" + task.getException().getMessage());
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                        System.out.println("SUCCESS:" + task.getResult());
                    }
                });

        // and start the StickItActivity
        Intent intent = new Intent(this, StickItActivity.class);
        intent.putExtra("username", username);

        System.out.println("CURRENT USERNAME LOGIN PAGE:" + username);

        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Save the current username in the textview with id username
        savedInstanceState.putString("username", ((TextView) findViewById(R.id.username_tv)).getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Restore the current username in the textview with id username
        ((TextView) findViewById(R.id.username_tv)).setText(savedInstanceState.getString("username"));
        ((EditText)findViewById(R.id.username_tv)).setSelection(((EditText)findViewById(R.id.username_tv)).getText().length());
    }
}