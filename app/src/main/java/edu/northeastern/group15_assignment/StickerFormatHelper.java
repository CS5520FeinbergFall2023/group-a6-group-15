package edu.northeastern.group15_assignment;

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
}
