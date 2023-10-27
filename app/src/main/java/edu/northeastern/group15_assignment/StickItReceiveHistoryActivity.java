package edu.northeastern.group15_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class StickItReceiveHistoryActivity extends AppCompatActivity {

    RecyclerView stickerReceiveHistoryView;

    ArrayList<StickerHistory> stickerHistory;

    StickerHistoryAdapter stickerHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it_receive_history);

        stickerReceiveHistoryView = findViewById(R.id.sticker_history_recycler_view);

        stickerHistory = new ArrayList<>();
        StickerHistory sth1 = new StickerHistory("HAPPY",
                "USHRIVAS", "10/26/2023");
        StickerHistory sth2 = new StickerHistory("SAD",
                "THAMEEM", "10/28/2023");
        StickerHistory sth3 = new StickerHistory("COOL",
                "KAIQI", "10/27/2023");
        StickerHistory sth4 = new StickerHistory("UNKNOWN",
                "RANDOMUN", "10/29/2023");
        stickerHistory.add(sth1);
        stickerHistory.add(sth2);
        stickerHistory.add(sth3);
        stickerHistory.add(sth4);


        stickerHistoryAdapter = new StickerHistoryAdapter(stickerHistory, this);

        stickerReceiveHistoryView.setLayoutManager(new LinearLayoutManager(this));
        stickerReceiveHistoryView.setAdapter(stickerHistoryAdapter);
    }
}