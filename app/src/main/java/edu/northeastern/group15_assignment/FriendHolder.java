package edu.northeastern.group15_assignment;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public FriendHolder(@NonNull android.view.View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.friend_name);
    }
}
