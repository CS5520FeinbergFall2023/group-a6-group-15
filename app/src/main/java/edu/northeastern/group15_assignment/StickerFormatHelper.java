package edu.northeastern.group15_assignment;

import android.content.Context;
import android.graphics.Bitmap;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;

public class StickerFormatHelper {
    public static StickerHistory toLocalFormat(Map<String, String> sticker) {

        System.out.println("DBG sticker: " + sticker);

        Timestamp stickerReceiveTime = new Timestamp(Long.parseLong(sticker.get("timestamp")));
        return new StickerHistory(
                sticker.get("sticker"),
                sticker.get("from"),
                stickerReceiveTime.toString()
        );
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        return android.graphics.BitmapFactory.decodeResource(context.getResources(), drawableId);
    }
}
