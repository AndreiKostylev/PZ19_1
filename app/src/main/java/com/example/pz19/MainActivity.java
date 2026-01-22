package com.example.pz19;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Context context;
    LayoutInflater layoutInflater;
    List<User> users;
    UserListAdapter userListAdapter;

    FrameLayout userPanel;
    TextView NameTextView, StateTextView, AgeTextView;
    Button backButton, editButton;

    private int positionActiveUser = -1;

    public static UserListAdapter staticAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new UserStaticInfo();
        users = UserStaticInfo.users;

        Init();
    }

    private void Init() {

        listView = findViewById(R.id.listView);

        context = this;
        layoutInflater = LayoutInflater.from(context);

        userPanel = findViewById(R.id.userPanel);
        NameTextView = findViewById(R.id.NameTextView);
        StateTextView = findViewById(R.id.StateTextView);
        AgeTextView = findViewById(R.id.AgeTextView);
        backButton = findViewById(R.id.backButton);
        editButton = findViewById(R.id.editButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserVisibility(false);
                positionActiveUser = -1;
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positionActiveUser != -1) {
                    GoToUserProfile(positionActiveUser);
                }
            }
        });

        userListAdapter = new UserListAdapter();
        staticAdapter = userListAdapter;
        listView.setAdapter(userListAdapter);
    }

    /**
     * Управляет видимостью панели пользователя
     */
    private void UserVisibility(boolean visible) {
        if (visible) {
            userPanel.setVisibility(View.VISIBLE);
        } else {
            userPanel.setVisibility(View.GONE);
        }
    }

    /**
     * Инициализирует панель пользователя данными
     */
    private void InitPanel(User item, int position) {
        NameTextView.setText(item.getName());
        StateTextView.setText(item.getState());
        AgeTextView.setText("Возраст: " + item.getAge());
        positionActiveUser = position;
        UserVisibility(true);
    }

    /**
     * Открывает активность редактирования пользователя
     * @param position позиция пользователя в списке
     */
    public void GoToUserProfile(int position) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(UserStaticInfo.POSITION, position);
        startActivity(intent);
    }

    /**
     * Обновляет список и панель пользователя
     */
    public static void UpdateListAndUserPanel(User user) {
        if (staticAdapter != null) {
            staticAdapter.notifyDataSetChanged();
        }

        // Если есть активный пользователь, обновляем панель
        if (MainActivity.getInstance() != null && user != null) {
            MainActivity.getInstance().updateUserPanel(user);
        }
    }

    /**
     * Обновляет только список
     */
    public static void UpdateList() {
        if (staticAdapter != null) {
            staticAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Обновляет панель пользователя
     */
    private void updateUserPanel(User user) {
        NameTextView.setText(user.getName());
        StateTextView.setText(user.getState());
        AgeTextView.setText("Возраст: " + user.getAge());
    }

    /**
     * Получает экземпляр MainActivity
     */
    private static MainActivity instance;

    @Override
    protected void onStart() {
        super.onStart();
        instance = this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        instance = null;
    }

    public static MainActivity getInstance() {
        return instance;
    }

    private class UserListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public User getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View currentView, ViewGroup parent) {
            User currentUser = getItem(position);

            currentView = layoutInflater.inflate(R.layout.item_user, parent, false);

            TextView nameView = currentView.findViewById(R.id.NameTextView);
            TextView stateView = currentView.findViewById(R.id.StateTextView);

            FrameLayout stateRound = currentView.findViewById(R.id.StateRound);

            nameView.setText(currentUser.getName());
            stateView.setText(currentUser.getState());


            switch (currentUser.getStateSignal()) {
                case 0:
                    stateRound.setBackgroundResource(R.drawable.back_offline);
                    break;
                case 1:
                    stateRound.setBackgroundResource(R.drawable.back_online);
                    break;
                case 2:
                    stateRound.setBackgroundResource(R.drawable.back_departed);
                    break;
            }

            currentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InitPanel(getItem(position), position);
                }
            });

            currentView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    GoToUserProfile(position);
                    return true;
                }
            });

            return currentView;
        }
    }
}
