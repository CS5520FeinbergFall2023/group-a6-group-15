package edu.northeastern.group15_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StickItReceiveHistoryActivity extends AppCompatActivity {

    RecyclerView stickerReceiveHistoryView;

    ArrayList<StickerHistory> stickerHistory;

    StickerHistoryAdapter stickerHistoryAdapter;
    FirebaseDatabase rootNode;
    String currentUser;

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
        setContentView(R.layout.activity_stick_it_receive_history);
        currentUser = getIntent().getStringExtra("username");

        rootNode = FirebaseDatabase.getInstance("https://a8-group15-new-default-rtdb.firebaseio.com/");

        stickerReceiveHistoryView = findViewById(R.id.sticker_history_recycler_view);

        stickerHistory = new ArrayList<>();
        DataSnapshot userRecvStickersSnapshot = getValueFromDataSnapshot(rootNode.getReference("username")
                .child(currentUser)
                .child("stickers_received")
                .get());

        int i = 0;
        for (DataSnapshot stickerSnapshot : userRecvStickersSnapshot.getChildren()) {
            if (i == 0) {
                i++;
                continue;
            }
            stickerHistory.add(StickerFormatHelper.toLocalFormat((Map<String, String>)stickerSnapshot.getValue()));
        }

        stickerHistoryAdapter = new StickerHistoryAdapter(stickerHistory, this);

        stickerReceiveHistoryView.setLayoutManager(new LinearLayoutManager(this));
        stickerReceiveHistoryView.setAdapter(stickerHistoryAdapter);
    }
}