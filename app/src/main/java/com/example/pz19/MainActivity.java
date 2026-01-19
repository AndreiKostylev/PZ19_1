package com.example.pz19;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Context context;
    LayoutInflater layoutInflater;
    List<User> users = new ArrayList<>();
    UserListAdapter userListAdapter;


    FrameLayout userPanel;
    TextView NameTextView, StateTextView, AgeTextView;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AddUsersInList();
        Init();
    }

    private void AddUsersInList() {
        users.add(new User("Иван", "В сети", 19, 1));
        users.add(new User("Мария", "Не в сети", 25, 0));
        users.add(new User("Алексей", "В сети", 22, 1));
        users.add(new User("Ольга", "Занят", 30, 2));
        users.add(new User("Дмитрий", "В сети", 28, 1));
        users.add(new User("Анна", "Отошёл", 24, 2));
        users.add(new User("Сергей", "Не в сети", 31, 0));
        users.add(new User("Елена", "В сети", 27, 1));
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


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserVisibility(false);
            }
        });


        userListAdapter = new UserListAdapter();
        listView.setAdapter(userListAdapter);
    }

    /**
     * Управляет видимостью панели пользователя
     * @param visible true - показать, false - скрыть
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
     * @param item пользователь для отображения
     */
    private void InitPanel(User item) {
        NameTextView.setText(item.getName());
        StateTextView.setText(item.getState());
        AgeTextView.setText("Возраст: " + item.getAge());
        UserVisibility(true);
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
                    InitPanel(getItem(position));
                }
            });

            return currentView;
        }
    }
}
