package com.example.pz19;
import android.content.Context;
import android.os.Vibrator;
public class Transform {
    public static int parseIntOrDefault(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public static Boolean StringNoNull(String string) {
        return string != null && string.length() != 0;
    }

    /**
     * Вызывает вибрацию
     */
    public static void Vibrate(Context context) {
        long mills = 1000; // 1 секунда
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(mills);
        }
    }
}