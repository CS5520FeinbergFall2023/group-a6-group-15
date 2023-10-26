package edu.northeastern.group15_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAboutClick(View view) {
        startActivity(new Intent(this, AboutGroupActivity.class));
    }

    public void onColorMindClick(View view) {
        startActivity(new Intent(this, ColorMindActivity.class));
    }

    public void onStickitClick(View view) {
        startActivity(new Intent(this, StickItLoginActivity.class));
    }
}