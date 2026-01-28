package com.example.pz19;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

public class Transform {

    public static int parseIntOrDefault(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            Log.d("Transform", "Ошибка преобразования строки в число: " + str);
            return defaultValue;
        }
    }

    /**
     * Проверяет, не пустая ли строка
     */
    public static Boolean StringNoNull(String string) {
        boolean result = string != null && string.length() != 0;
        Log.d("Transform", "StringNoNull: " + string + " -> " + result);
        return result;
    }

    /**
     * Вызывает вибрацию
     */
    public static void Vibrate(Context context) {
        long mills = 1000; // 1 секунда
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(mills);
            Log.d("Transform", "Вибрация вызвана");
        } else {
            Log.d("Transform", "Вибрация не поддерживается");
        }
    }
}