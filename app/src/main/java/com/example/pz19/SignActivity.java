package com.example.pz19;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignActivity extends AppCompatActivity {

    private EditText LoginTextView;
    private EditText PasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        LoginTextView = findViewById(R.id.LoginTextView);
        PasswordTextView = findViewById(R.id.PasswordTextView);
    }

    public void SignIn(View view) {
        String login = getLogin();
        String password = getPassword();

        // Проверка на пустые поля
        if (!Transform.StringNoNull(login) || !Transform.StringNoNull(password)) {
            Transform.Vibrate(this);
            Toast.makeText(this,
                    getString(R.string.NullParametersMessage),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(UserStaticInfo.USERS_SIGN_IN_INFO);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.child(login).child(UserStaticInfo.PASSWORD).getValue();

                if (value != null) {
                    if (value.toString().equals(password)) {
                        // Успешный вход
                        Object profileIdObj = dataSnapshot.child(login).child(UserStaticInfo.PROFILE_ID).getValue();
                        if (profileIdObj != null) {
                            goNext(profileIdObj.toString());
                        } else {
                            CantSignIn();
                        }
                    } else {
                        CantSignIn();
                    }
                } else {
                    CantSignIn();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SignActivity.this,
                        "Ошибка подключения к базе данных",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goNext(String profileId) {
        UserStaticInfo.profileId = profileId;

        // Загружаем профиль пользователя из Firebase
        loadUserProfile(profileId);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadUserProfile(String profileId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference profileRef = database.getReference(UserStaticInfo.USERS_PROFILE_INFO);

        profileRef.child(profileId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child(UserStaticInfo.NAME).getValue(String.class);
                    String state = dataSnapshot.child(UserStaticInfo.STATE).getValue(String.class);
                    Integer age = dataSnapshot.child(UserStaticInfo.AGE).getValue(Integer.class);

                    if (name != null && state != null && age != null) {
                        // Добавляем пользователя в статический список
                        User user = new User(name, state, age, 1); // StateSignal = 1 (online)
                        UserStaticInfo.users.clear(); // Очищаем старых пользователей
                        UserStaticInfo.users.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Ошибка загрузки профиля
            }
        });
    }

    private void CantSignIn() {
        Toast.makeText(SignActivity.this,
                getString(R.string.wrongLoginOrPassword),
                Toast.LENGTH_SHORT).show();
    }

    private String getLogin() {
        return LoginTextView.getText().toString();
    }

    private String getPassword() {
        return PasswordTextView.getText().toString();
    }
}