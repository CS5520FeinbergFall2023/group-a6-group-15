package edu.northeastern.group15_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class StickItCountActivity extends AppCompatActivity {

    FirebaseDatabase rootNode;
    String currentUser = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_count);

        // Get the username from the intent
        this.currentUser = getIntent().getStringExtra("username");

        rootNode = FirebaseDatabase.getInstance("https://a8-group15-new-default-rtdb.firebaseio.com/");
        DataSnapshot user = getValueFromDataSnapshot(rootNode.getReference("username").child(currentUser).get());

        // TODO: Get the sticker counts from the user's database entry
        Map<String, Long> stickerCounts = (Map<String, Long>) user.child("sticker_counts").getValue();

        // TODO: Update the textviews with the sticker counts
        TextView happyCount = (TextView) findViewById(R.id.happy_count_tv);
        TextView sadCount = (TextView) findViewById(R.id.sad_count_tv);
        TextView coolCount = (TextView) findViewById(R.id.cool_count_tv);

        happyCount.setText("HAPPY: " + stickerCounts.get("HAPPY"));
        sadCount.setText("SAD: " + stickerCounts.get("SAD"));
        coolCount.setText("COOL: " + stickerCounts.get("COOL"));
    }
}