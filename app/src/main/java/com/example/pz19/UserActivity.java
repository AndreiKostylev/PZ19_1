package com.example.pz19;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class UserActivity extends AppCompatActivity {

    private EditText NameEditText;
    private EditText StateEditText;
    private EditText AgeEditText;

    private Button backButton;
    private Button saveButton;

    private User activeUser;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Убрать ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Log.d("UserActivity", "onCreate: запуск UserActivity");

        position = getIntent().getIntExtra(UserStaticInfo.POSITION, 0);
        Log.d("UserActivity", "Получена позиция: " + position);

        if (position >= 0 && position < UserStaticInfo.users.size()) {
            activeUser = UserStaticInfo.users.get(position);
            Log.d("UserActivity", "Найден пользователь: " +
                    (activeUser != null ? activeUser.getName() : "null"));
        } else {
            Log.e("UserActivity", "Неверная позиция или пустой список пользователей");
            finish();
            return;
        }

        Init();
        setUserInfo();
    }

    private void Init() {
        NameEditText = findViewById(R.id.NameEditText);
        StateEditText = findViewById(R.id.StateTextView);
        AgeEditText = findViewById(R.id.AgeEditText);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
    }

    private void setUserInfo() {
        if (activeUser != null) {
            NameEditText.setText(activeUser.getName());
            StateEditText.setText(activeUser.getState());
            AgeEditText.setText(String.valueOf(activeUser.getAge()));
            Log.d("UserActivity", "Установлены данные пользователя: " + activeUser.getName());
        }
    }

    /**
     * Сохранение изменений пользователя
     */
    private void Save() {
        if (activeUser != null) {
            Log.d("UserActivity", "Сохранение изменений для: " + activeUser.getName());

            String newName = NameEditText.getText().toString();
            String newState = StateEditText.getText().toString();
            String newAgeStr = AgeEditText.getText().toString();

            if (!newName.isEmpty()) {
                activeUser.setName(newName);
            }
            if (!newState.isEmpty()) {
                activeUser.setState(newState);
            }

            if (!newAgeStr.isEmpty()) {
                try {
                    int newAge = Integer.parseInt(newAgeStr);
                    activeUser.setAge(newAge);
                } catch (NumberFormatException e) {
                    activeUser.setAge(activeUser.getAge());
                }
            }

            // Обновляем пользователя в статическом списке
            UserStaticInfo.updateUser(position, activeUser);

            // Обновляем список в MainActivity
            MainActivity.UpdateListAndUserPanel(activeUser);

            // Также обновляем в Firebase
            updateUserInFirebase();

            finish();
        }
    }

    private void updateUserInFirebase() {
        // В этом методе можно добавить обновление пользователя в Firebase
        // Но для простоты сейчас обновляем только локальный список
        Log.d("UserActivity", "Пользователь обновлен локально");
    }
}