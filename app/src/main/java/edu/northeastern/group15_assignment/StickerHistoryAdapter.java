package edu.northeastern.group15_assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class StickerHistoryAdapter extends RecyclerView.Adapter<StickerHistoryAdapter.StickerHistoryViewHolder> {

    private final List<StickerHistory> stickerHistory;

    private final Context context;

    public StickerHistoryAdapter(List<StickerHistory> links, Context context){
        this.stickerHistory = links;
        this.context = context;
    }


    @NonNull
    @Override
    public StickerHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StickerHistoryViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.sticker_history_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull StickerHistoryViewHolder holder, int position) {
        int emoji_sticker;
        StickerHistory history = stickerHistory.get(position);
        if(history.emojiType.equals("HAPPY")){
            emoji_sticker = R.drawable.happy_emoji;
        }
        else if(history.emojiType.equals("SAD")){
            emoji_sticker = R.drawable.sad_emoji;
        }
        else if(history.emojiType.equals("COOL")){
            emoji_sticker = R.drawable.cool_emoji;
        }
        else {
            emoji_sticker = R.drawable.unknown_sticker;
        }

        holder.sticker_view.setImageResource(emoji_sticker);
        holder.sent_by_tv.setText(stickerHistory.get(position).sentBy);
        holder.sticker_receive_time.setText(stickerHistory.get(position).stickerReceiveTime);
    }

    @Override
    public int getItemCount() {
        return stickerHistory.size();
    }

    public class StickerHistoryViewHolder extends RecyclerView.ViewHolder {
        public ImageView sticker_view;
        public TextView sent_by_tv;
        public TextView sticker_receive_time;
        int position;

        public StickerHistoryViewHolder(@NonNull View stickerHistoryView) {
            super(stickerHistoryView);
            this.sticker_view = stickerHistoryView.findViewById(R.id.emoji_history);
            this.sent_by_tv = stickerHistoryView.findViewById(R.id.sticker_sent_by_tv);
            this.sticker_receive_time = stickerHistoryView.findViewById(R.id.sticker_received_time_tv);
        }
    }
}


















