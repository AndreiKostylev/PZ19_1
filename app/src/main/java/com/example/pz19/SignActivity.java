package com.example.pz19;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignActivity extends AppCompatActivity {

    private EditText LoginTextView, PasswordTextView;
    private EditText NewLoginTextView, NewPasswordTextView, NewAgeTextView,
            NewNameTextView, NewStateTextView;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        // Убрать ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Init();
    }

    private void Init() {
        // Инициализация TabHost
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        // Создаем вкладки с кастомным видом
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("signIn");
        tabSpec.setContent(R.id.tabSignIn);

        // Создаем кастомный View для вкладки
        TextView tab1 = new TextView(this);
        tab1.setText("ВХОД");
        tab1.setGravity(Gravity.CENTER);
        tab1.setPadding(20, 20, 20, 20);
        tab1.setTextSize(14);
        tabSpec.setIndicator(tab1);
        tabHost.addTab(tabSpec);

        // Вторая вкладка
        tabSpec = tabHost.newTabSpec("signUp");
        tabSpec.setContent(R.id.tabSignUp);

        TextView tab2 = new TextView(this);
        tab2.setText("РЕГИСТРАЦИЯ");
        tab2.setGravity(Gravity.CENTER);
        tab2.setPadding(20, 20, 20, 20);
        tab2.setTextSize(14);
        tabSpec.setIndicator(tab2);
        tabHost.addTab(tabSpec);

        // Устанавливаем обработчик изменения вкладок для изменения цвета текста
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                updateTabColors();
            }
        });

        // Устанавливаем первую вкладку активной
        tabHost.setCurrentTab(0);
        updateTabColors();

        // Инициализация полей входа
        LoginTextView = findViewById(R.id.LoginTextView);
        PasswordTextView = findViewById(R.id.PasswordTextView);

        // Инициализация полей регистрации
        NewLoginTextView = findViewById(R.id.NewLoginTextView);
        NewPasswordTextView = findViewById(R.id.NewPasswordTextView);
        NewNameTextView = findViewById(R.id.NewNameTextView);
        NewAgeTextView = findViewById(R.id.NewAgeTextView);
        NewStateTextView = findViewById(R.id.NewStateTextView);
    }

    private void updateTabColors() {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View tabView = tabHost.getTabWidget().getChildAt(i);
            TextView tabTextView = (TextView) tabView.findViewById(android.R.id.title);
            if (tabTextView != null) {
                if (i == tabHost.getCurrentTab()) {
                    // Активная вкладка
                    tabTextView.setTextColor(getResources().getColor(android.R.color.black));
                    tabTextView.setTypeface(null, Typeface.BOLD);
                } else {
                    // Неактивная вкладка
                    tabTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    tabTextView.setTypeface(null, Typeface.NORMAL);
                }
            }
        }
    }

    // ========== МЕТОДЫ ВХОДА ==========

    public void SignIn(View view) {
        String login = getLogin();
        String password = getPassword();

        Log.d("SignIn", "Попытка входа: " + login);

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
                        Object profileIdObj = dataSnapshot.child(login).child(UserStaticInfo.PROFILE_ID).getValue();
                        if (profileIdObj != null) {
                            UserStaticInfo.profileId = profileIdObj.toString();
                            Log.d("SignIn", "Успешный вход, profileId: " + profileIdObj.toString());
                            loadAllUsersFromFirebase(); // Загружаем ВСЕХ пользователей
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
                Log.e("Firebase", "Ошибка подключения: " + error.getMessage());
                Toast.makeText(SignActivity.this,
                        "Ошибка подключения к базе данных",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllUsersFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference profileRef = database.getReference(UserStaticInfo.USERS_PROFILE_INFO);

        Log.d("Firebase", "Начинаю загрузку всех пользователей...");

        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Очищаем старый список
                UserStaticInfo.users.clear();

                // Загружаем ВСЕХ пользователей из Firebase
                int count = 0;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    try {
                        String name = userSnapshot.child(UserStaticInfo.NAME).getValue(String.class);
                        String state = userSnapshot.child(UserStaticInfo.STATE).getValue(String.class);
                        Integer age = userSnapshot.child(UserStaticInfo.AGE).getValue(Integer.class);
                        Integer stateSignal = userSnapshot.child(UserStaticInfo.STATE_SIGNAL).getValue(Integer.class);

                        if (name != null && state != null && age != null) {
                            int signal = (stateSignal != null) ? stateSignal : 1; // По умолчанию онлайн
                            User user = new User(name, state, age, signal);
                            UserStaticInfo.users.add(user);
                            count++;
                            Log.d("Firebase", "Загружен пользователь: " + name);
                        }
                    } catch (Exception e) {
                        Log.e("Firebase", "Ошибка загрузки пользователя: " + e.getMessage());
                    }
                }

                Log.d("Firebase", "Всего загружено пользователей: " + count);

                // Запускаем MainActivity
                Intent intent = new Intent(SignActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Ошибка загрузки пользователей: " + error.getMessage());
                Toast.makeText(SignActivity.this,
                        "Ошибка загрузки пользователей",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CantSignIn() {
        Toast.makeText(SignActivity.this,
                getString(R.string.wrongLoginOrPassword),
                Toast.LENGTH_SHORT).show();
    }

    // ========== МЕТОДЫ РЕГИСТРАЦИИ ==========

    public void SignUp(View view) {
        String newLogin = getNewLogin();
        String newPassword = getNewPassword();
        String newName = getNewName();
        String newState = getNewState();
        int newAge = getNewAge();

        // Проверка на пустые поля
        if (!Transform.StringNoNull(newLogin) ||
                !Transform.StringNoNull(newPassword) ||
                !Transform.StringNoNull(newName) ||
                !Transform.StringNoNull(newState) ||
                newAge <= 0) {

            Transform.Vibrate(this);
            Toast.makeText(this,
                    getString(R.string.fillAllFields),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверяем, существует ли уже пользователь
        checkUserExistsAndRegister(newLogin, newPassword, newName, newState, newAge);
    }

    private void checkUserExistsAndRegister(String login, String password,
                                            String name, String state, int age) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference(UserStaticInfo.USERS_SIGN_IN_INFO);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(login)) {
                    // Пользователь уже существует
                    Toast.makeText(SignActivity.this,
                            getString(R.string.userExists),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Регистрируем нового пользователя
                    registerNewUser(login, password, name, state, age);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SignActivity.this,
                        "Ошибка проверки пользователя",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerNewUser(String login, String password,
                                 String name, String state, int age) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference(UserStaticInfo.USERS_SIGN_IN_INFO);
        DatabaseReference profilesRef = database.getReference(UserStaticInfo.USERS_PROFILE_INFO);

        // Генерируем новый profileId
        String newProfileId = generateNewProfileId();

        // Сохраняем данные для входа
        DatabaseReference newUserRef = usersRef.child(login);
        newUserRef.child(UserStaticInfo.PASSWORD).setValue(password);
        newUserRef.child(UserStaticInfo.PROFILE_ID).setValue(newProfileId);

        // Сохраняем профиль пользователя
        DatabaseReference newProfileRef = profilesRef.child(newProfileId);
        newProfileRef.child(UserStaticInfo.NAME).setValue(name);
        newProfileRef.child(UserStaticInfo.AGE).setValue(age);
        newProfileRef.child(UserStaticInfo.STATE).setValue(state);
        newProfileRef.child(UserStaticInfo.STATE_SIGNAL).setValue(1); // По умолчанию онлайн

        // Успешная регистрация
        Toast.makeText(this,
                getString(R.string.registrationSuccess),
                Toast.LENGTH_SHORT).show();

        // Переключаемся на вкладку входа
        tabHost.setCurrentTab(0);

        // Очищаем поля регистрации
        clearRegistrationFields();
    }

    private String generateNewProfileId() {
        // Простой способ генерации ID - текущее время в миллисекундах
        return String.valueOf(System.currentTimeMillis());
    }

    private void clearRegistrationFields() {
        NewLoginTextView.setText("");
        NewPasswordTextView.setText("");
        NewNameTextView.setText("");
        NewAgeTextView.setText("");
        NewStateTextView.setText("");
    }

    // ========== GETTER МЕТОДЫ ==========

    private String getLogin() {
        return LoginTextView.getText().toString();
    }

    private String getPassword() {
        return PasswordTextView.getText().toString();
    }

    private String getNewLogin() {
        return NewLoginTextView.getText().toString();
    }

    private String getNewPassword() {
        return NewPasswordTextView.getText().toString();
    }

    private int getNewAge() {
        try {
            return Transform.parseIntOrDefault(NewAgeTextView.getText().toString(), 0);
        } catch (Exception e) {
            return 0;
        }
    }

    private String getNewName() {
        return NewNameTextView.getText().toString();
    }

    private String getNewState() {
        return NewStateTextView.getText().toString();
    }
}