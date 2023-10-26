package edu.northeastern.group15_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class StickItActivity extends AppCompatActivity {

    String stickerSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick_it);
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
    }

    public void onStickerCountSentClick(View view){
        startActivity(new Intent(this, StickItCountActivity.class));
    }

    public void onStickersGotClick(View view){
        startActivity(new Intent(this, StickItReceiveHistoryActivity.class));
    }
}