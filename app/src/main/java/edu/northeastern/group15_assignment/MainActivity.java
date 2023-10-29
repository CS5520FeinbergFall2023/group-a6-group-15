package edu.northeastern.group15_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyApp";
    FirebaseDatabase rootNode;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAboutClick(View view) {

        startActivity(new Intent(this, AboutGroupActivity.class));

        rootNode = FirebaseDatabase.getInstance();

        // Connect to the database
        rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/").setValue("Hello, World!").addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                System.out.println("failed");
                System.out.println(task.getException());
            }
            else {
                System.out.println("success");
                System.out.println(task.getResult());
            }
        });

        reference = rootNode.getReference("message");
    }

    public void onColorMindClick(View view) {
        startActivity(new Intent(this, ColorMindActivity.class));

    }

    public void onStickitClick(View view) {
        startActivity(new Intent(this, StickItLoginActivity.class));
    }
}