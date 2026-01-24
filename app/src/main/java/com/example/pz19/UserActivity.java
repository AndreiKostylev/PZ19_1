package com.example.pz19;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    private EditText NameEditText;
    private EditText StateTextView;
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
        StateTextView = findViewById(R.id.StateTextView);
        AgeEditText = findViewById(R.id.AgeEditText);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        backButton.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> Save());
    }

    private void setUserInfo() {
        if (activeUser != null) {
            NameEditText.setText(activeUser.getName());
            StateTextView.setText(activeUser.getState());
            AgeEditText.setText(String.valueOf(activeUser.getAge()));
        }
    }

    public void Save() {
        if (activeUser != null) {
            activeUser.setName(NameEditText.getText().toString());
            activeUser.setState(StateTextView.getText().toString());
            String age = AgeEditText.getText().toString();
            activeUser.setAge(Transform.parseIntOrDefault(age, activeUser.getAge()));

            UserStaticInfo.updateUser(position, activeUser);
            MainActivity.UpdateListAndUserPanel(activeUser);
            finish();
        }
    }
}