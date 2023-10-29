package edu.northeastern.group15_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StickItActivity extends AppCompatActivity {

    String stickerSelected = "";
    List<String> validStickers = List.of("HAPPY", "SAD", "COOL");

    FirebaseDatabase rootNode;
    String currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it);

        // Initialize FirebaseApp
        rootNode = FirebaseDatabase.getInstance("https://a8-group15-new-default-rtdb.firebaseio.com/");

        currentUser = getIntent().getStringExtra("username");
    }

    public void onHappyClick(View view){
        this.stickerSelected = "HAPPY";
    }

    public void onSadClick(View view){
        this.stickerSelected = "SAD";
    }

    public void onCoolClick(View view){
        this.stickerSelected = "COOL";
    }

    public void sendStickerClick(View view){
        Log.i("SENDING STICKER", this.stickerSelected);

        // Validate the presence of a sticker in the sticker list of the database
        if (!validStickers.contains(this.stickerSelected)) {
            // If the sticker is not present, display a snackbar with the message "Sticker not found"
            // and return
            Log.i("STICKER NOT FOUND", this.stickerSelected);
            return;
        }

        // Retrieve the username of the user from the edittext with id username
        String username = ((EditText) findViewById(R.id.sendto_uname)).getText().toString();

        // Validate that such a user exists in the database
        rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/")
                .child("username")
                .child(username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("USER FOUND", task.getResult().getValue().toString());
                    } else {
                        Log.i("USER NOT FOUND", task.getResult().getValue().toString());
                        Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG).show();
                    }
                });

        // Check if the intended user is a friend of the current user
        if (rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/")
                .child("username")
                .child(currentUser)
                .child("friends")
                .child(username)
                .get()
                .isSuccessful()) {
            Log.i("USER IS A FRIEND", username);
        } else {
            Log.i("USER IS NOT A FRIEND", username);
            Toast.makeText(getApplicationContext(), "User is not a friend", Toast.LENGTH_LONG).show();
        }

        // If the user is not a friend, display a toast with the message "User is not a friend"

        // If the user exists, add the sticker to the user's sticker list and increment the count in the current user's sticker count

    }

    // History of stickers sent by the user
    public void onStickerCountSentClick(View view){
        startActivity(new Intent(this, StickItCountActivity.class));
    }

    // History of stickers received by the user
    public void onStickersGotClick(View view){
        startActivity(new Intent(this, StickItReceiveHistoryActivity.class));
    }

    // Friends of the user
    public void onManageFriendsClick(View view){
        startActivity(new Intent(this, UserFriendsManage.class));
    }
}