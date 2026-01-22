package com.example.pz19;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

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


        position = getIntent().getIntExtra(UserStaticInfo.POSITION, 0);

        if (position >= 0 && position < UserStaticInfo.users.size()) {
            activeUser = UserStaticInfo.users.get(position);
        }

        Init();


        setUserInfo();
    }

    private void Init() {

        NameEditText = findViewById(R.id.NameEditText);
        StateEditText = findViewById(R.id.StateEditText);
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
        }
    }

    /**
     * Сохранение изменений пользователя
     */
    private void Save() {
        if (activeUser != null) {

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

            UserStaticInfo.updateUser(position, activeUser);

            MainActivity.UpdateListAndUserPanel(activeUser);

            finish();
        }
    }
}