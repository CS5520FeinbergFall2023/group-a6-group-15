package edu.northeastern.group15_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        TextView banner = (TextView) findViewById(R.id.stick_it_banner);
        banner.setText("Welcome, " + currentUser + "!");
    }

    private void sendSticker(String stickerSelected, String friend) {
        // TODO: Send sticker to friend
    }

    public void onHappyClick(View view){
        this.stickerSelected = "HAPPY";
        String sendTo = ((EditText) findViewById(R.id.sendto_uname)).getText().toString();
        sendSticker(this.stickerSelected, sendTo);
    }

    public void onSadClick(View view){
        this.stickerSelected = "SAD";
        String sendTo = ((EditText) findViewById(R.id.sendto_uname)).getText().toString();
        sendSticker(this.stickerSelected, sendTo);
    }

    public void onCoolClick(View view){
        this.stickerSelected = "COOL";
        String sendTo = ((EditText) findViewById(R.id.sendto_uname)).getText().toString();
        sendSticker(this.stickerSelected, sendTo);
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

        System.out.println("USERNAME SENDTO: " + username);

        DataSnapshot friendsDataSnapshot = getValueFromDataSnapshot(
                rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/")
                .child("username")
                .child(currentUser)
                .child("friends")
                .get());

        System.out.println("FRIENDS DATA SNAPSHOT: " + friendsDataSnapshot.getValue());
        List<String> allFriends = (List<String>) friendsDataSnapshot.getValue();

        if (allFriends == null) {
            allFriends = List.of();
        }

        // Check if the intended user is a friend of the current user
        if (!allFriends.contains(username)) {
            Log.i("USER IS NOT A FRIEND", username);
            Toast.makeText(getApplicationContext(), "User is not a friend", Toast.LENGTH_LONG).show();
        } else {
            Log.i("USER IS A FRIEND", username);

            // Insert the sticker into the user's sticker list
            DataSnapshot userStickersDataSnapshot = getValueFromDataSnapshot(
                    rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/")
                            .child("username")
                            .child(username)
                            .child("stickers_received")
                            .get());
            List<Map<String, String>> userStickers = (List<Map<String, String>>) userStickersDataSnapshot.getValue();
            if (userStickers == null) {
                userStickers = List.of();
            }

            // Send the sticker to the intended user by adding it to the user's sticker list
            // Add timestamp, sticker, and from to the sticker
            userStickers.add(Map.of("timestamp", Long.toString(System.currentTimeMillis()), "sticker", this.stickerSelected, "from", currentUser));

            // Increment the sticker count in the current user's sticker count
            DataSnapshot currentUserStickerCountDataSnapshot = getValueFromDataSnapshot(
                    rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/")
                            .child("username")
                            .child(currentUser)
                            .child("sticker_counts")
                            .get());
            Map<String, Long> stickerCounts = (Map<String, Long>) currentUserStickerCountDataSnapshot.getValue();
            // Clone the sticker counts
            if (stickerCounts == null) {
                System.out.println("STICKER COUNTS IS NULL");
                stickerCounts = Map.of("HAPPY", Integer.toUnsignedLong(0), "SAD", Integer.toUnsignedLong(0), "COOL", Integer.toUnsignedLong(0));
            }
            Map<String, Long> stickerCountsClone = new HashMap<>();
            stickerCountsClone.put("HAPPY", (Long) stickerCounts.get("HAPPY"));
            stickerCountsClone.put("SAD", (Long) stickerCounts.get("SAD"));
            stickerCountsClone.put("COOL", (Long) stickerCounts.get("COOL"));
            stickerCountsClone.put(this.stickerSelected, stickerCountsClone.get(this.stickerSelected) + 1);

            // Update the sticker list and sticker count in the database
            rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/")
                    .child("username")
                    .child(username)
                    .child("stickers_received")
                    .setValue(userStickers)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            // FAILURE
                            // Make a toast
                            Toast.makeText(getApplicationContext(), "Failed to send sticker. Please try again", Toast.LENGTH_LONG).show();
                            System.out.println("ERROR:" + task.getException().getMessage());
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Sticker sent successfully", Toast.LENGTH_LONG).show();
                            System.out.println("SUCCESS:" + task.getResult());
                        }
                    });
            rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/")
                    .child("username")
                    .child(currentUser)
                    .child("sticker_counts")
                    .setValue(stickerCountsClone)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            // FAILURE
                            // Make a toast
                            Toast.makeText(getApplicationContext(), "Failed to send sticker. Please try again", Toast.LENGTH_LONG).show();
                            System.out.println("ERROR:" + task.getException().getMessage());
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Sticker sent successfully", Toast.LENGTH_LONG).show();
                            System.out.println("SUCCESS:" + task.getResult());
                        }
                    }
            );
        }
    }

    // History of stickers sent by the user
    public void onStickerCountSentClick(View view){
        Intent intent = new Intent(this, StickItCountActivity.class);
        intent.putExtra("username", currentUser);
        startActivity(intent);
    }

    // History of stickers received by the user
    public void onStickersGotClick(View view){
        Intent intent = new Intent(this, StickItReceiveHistoryActivity.class);
        intent.putExtra("username", currentUser);
        startActivity(intent);
    }

    // Friends of the user
    public void onManageFriendsClick(View view){
        Intent intent = new Intent(this, UserFriendsManageActivity.class);
        intent.putExtra("username", currentUser);
        startActivity(intent);
    }
}