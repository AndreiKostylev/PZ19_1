package com.example.pz19;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

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

    /**
     * НОВЫЙ МЕТОД: Проверка EditText с анимацией
     */
    public static boolean EditTextNoNullWithAnimation(Context context, EditText editText, int animationResId) {
        boolean hasText = StringNoNull(editText.getText().toString());
        if (!hasText) {
            Animation animation = AnimationUtils.loadAnimation(context, animationResId);
            editText.startAnimation(animation);
            editText.setError("Это поле обязательно для заполнения");

            // Короткая вибрация
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(200);
            }
        } else {
            editText.setError(null);
        }
        return hasText;
    }
}