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

//        //        todo: test
//        rootNode = FirebaseDatabase.getInstance();
////        Log.i(TAG, "print root node in logcat");
//        System.out.println("print root node");
//        System.out.println("root node info is down below");
//        System.out.println(rootNode);
//        reference = rootNode.getReference("message");
//        System.out.println(reference);
////        reference.child("message").setValue("Hello world!!");
//        reference.setValue("Hello, World!");
//        reference.setValue("Hello????").addOnCompleteListener(new OnCompleteListener<Void>(){
//
//            @Override
//            public void onComplete(@NonNull Task<Void> task){
//                // SUCCESS
//
//                // Log the details
//                Log.d("FirebaseData","user data uploaded successfully");
//                // Make a toast
//                Toast.makeText(getApplicationContext(), "user data uploaded successfully", Toast.LENGTH_LONG).show();
//            }
//
//        }).addOnFailureListener(new OnFailureListener(){
//            @Override
//            public void onFailure(@NonNull Exception e){
//                // FAILURE
//
//                // Log the details
//                Log.d("FirebaseData","user data upload failed");
//                // Make a toast
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

//        Read
//        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });


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


        // Get the current value in the database
//        reference.get().addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                System.out.println("failed");
//                System.out.println(task.getException());
//            }
//            else {
//                System.out.println("success");
//                System.out.println(task.getResult().getValue());
//            }
//        });

        System.out.println("reference");
        System.out.println(reference);

//        try {
//            Task result = reference.setValue("Fire data storage");
//
//            while(!result.isComplete()) {
//                // sleep for 5 ms
//                try {
//                    Thread.sleep(5);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            result.addOnFailureListener(e -> {
//                System.out.println("failed");
//                System.out.println(e);
//            });
//            result.addOnSuccessListener(e -> {
//                System.out.println("success");
//                System.out.println(e);
//            });
//            System.out.println("resulthfdsougfigdfiy");
//            System.out.println();
//        } catch (Exception e) {
//            System.out.println("\n\n\n!!!error!!!");
//            System.out.println(e);
//        }

        System.out.println("so far so good");

    }

    public void onColorMindClick(View view) {
        startActivity(new Intent(this, ColorMindActivity.class));

    }

    public void onStickitClick(View view) {
        startActivity(new Intent(this, StickItLoginActivity.class));
    }
}