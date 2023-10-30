package edu.northeastern.group15_assignment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendHolder> {

    private final List<String> friends;
    private final Context context;

    public FriendAdapter(List<String> friends, Context context) {
        this.friends = friends;
        this.context = context;
    }

    @Override
    public FriendHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
        android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friendnameholder, parent, false);
        return new FriendHolder(view);
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

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        if (position == 0) {
            holder.itemView.setLayoutParams(
                    new LinearLayout.LayoutParams(0,0)
            );
        }
        // TODO: Set the name of the friend to the textview
        holder.name.setText(friends.get(position));
        holder.itemView.setOnLongClickListener(view -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());
            builder.setTitle("Delete Friend");
            builder.setPositiveButton("Delete", (dialogInterface, i) -> {
                friends.remove(position);

                // Delete the item in the list in the DB
                FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://a8-group15-new-default-rtdb.firebaseio.com/");
                // Def working on a best-effort basis
                // TODO: Retrieve the list, update and set the list
                // Retrieve the current user's friends list
                DatabaseReference currentUserFriends = rootNode.getReferenceFromUrl("https://a8-group15-new-default-rtdb.firebaseio.com/").getRef()
                        .child("username")
                        .child(friends.get(position))
                        .child("friends");

                // Add the new friend to the current user's friends list
                // Get the whole list
                List<String> friendsList = (List<String>) getValueFromDataSnapshot(currentUserFriends.get()).getValue();

                friendsList.remove(friends.get(position));
                currentUserFriends.setValue(friendsList);
            });
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                // Do nothing
            });
            builder.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}