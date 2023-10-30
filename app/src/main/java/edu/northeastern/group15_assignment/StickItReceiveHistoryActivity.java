package edu.northeastern.group15_assignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StickItReceiveHistoryActivity extends AppCompatActivity {

    RecyclerView stickerReceiveHistoryView;

    ArrayList<StickerHistory> stickerHistory;

    StickerHistoryAdapter stickerHistoryAdapter;
    FirebaseDatabase rootNode;
    String currentUser;
    Integer currentStickerCount;

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
        currentStickerCount = 0;
        currentUser = getIntent().getStringExtra("username");

        rootNode = FirebaseDatabase.getInstance("https://a8-group15-new-default-rtdb.firebaseio.com/");

        stickerReceiveHistoryView = findViewById(R.id.sticker_history_recycler_view);

        DatabaseReference ref = rootNode.getReference("username").child(currentUser).child("stickers_received");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    currentStickerCount = (int) snapshot.getChildrenCount();
                }
                System.out.println("DBG sticker count::" + currentStickerCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("DBG child added::" + snapshot);
                if (snapshot.getKey().equals("0") || snapshot.getValue() == null)
                    return;
                StickerHistory newStickerRecv = StickerFormatHelper.toLocalFormat((Map<String, String>) snapshot.getValue());
                stickerHistory.add(newStickerRecv);
                stickerHistoryAdapter.notifyDataSetChanged();

                Bitmap stickerBitmap = null;

                if (newStickerRecv.emojiType.equals("HAPPY")) {
                    stickerBitmap = StickerFormatHelper.getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.happy_emoji);
                } else if (newStickerRecv.emojiType.equals("SAD")) {
                    stickerBitmap = StickerFormatHelper.getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.sad_emoji);
                } else if (newStickerRecv.emojiType.equals("COOL")) {
                    stickerBitmap = StickerFormatHelper.getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.cool_emoji);
                } else {
                    stickerBitmap = StickerFormatHelper.getBitmapFromVectorDrawable(getApplicationContext(), R.drawable.unknown_sticker);
                }

                NotificationChannel channel = null;


                channel = new NotificationChannel(
                            "New_Updates",
                            "New_Updates",
                            NotificationManager.IMPORTANCE_HIGH
                );
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);


                Notification notif = new NotificationCompat.Builder(
                        getApplicationContext(),
                        channel.getId()
                ).setContentTitle("New Sticker Received")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentText(
                                "You have received a new sticker from " +
                                        newStickerRecv.sentBy
                        )
                        .setStyle(
                                new NotificationCompat.BigPictureStyle()
                                        .setSummaryText(
                                                "You have received a new sticker from " +
                                                        newStickerRecv.sentBy
                                        ).bigPicture(stickerBitmap)
                                        .bigLargeIcon(null))
                        .build();
//                        );


                // Initiate notification
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
//                       public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                              int[] grantResults)
//                     to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    // Request for notification permission
                    ActivityCompat.requestPermissions(
                            StickItReceiveHistoryActivity.this
                            , new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);

                    return;
                } else {
                    System.out.println("PERMISSION GRANTED");
                }

                notificationManagerCompat.notify(1, notif);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("DBG child changed::" + snapshot.getValue());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                System.out.println("DBG child removed::" + snapshot.getValue());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("DBG child moved::" + snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        stickerHistory = new ArrayList<>();
        DataSnapshot userRecvStickersSnapshot = getValueFromDataSnapshot(ref.get());



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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Convert the list to an arraylist of arraylist and save it in the bundle
        outState.putString("username", currentUser);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Retrieve the arraylist of arraylist from the bundle and convert it to a list of Link objects
        currentUser = savedInstanceState.getString("username");
    }
}