package com.example.pz19;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Переменные
    ListView listView;
    Context context;
    LayoutInflater layoutInflater;

    // Список пользователей
    List<User> users = new ArrayList<>();

    // Адаптер для отображения
    UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Добавляем пользователей в список
        AddUsersInList();

        // Инициализация
        Init();
    }

    private void AddUsersInList() {
        // Добавляем тестовых пользователей
        users.add(new User("Иван", "В сети", 19));
        users.add(new User("Мария", "Не в сети", 25));
        users.add(new User("Алексей", "В сети", 22));
        users.add(new User("Ольга", "Занят", 30));
        users.add(new User("Дмитрий", "В сети", 28));
    }

    private void Init() {
        // Получаем ListView
        listView = findViewById(R.id.listView);

        // Инициализируем контекст
        context = this;

        // Инициализируем LayoutInflater
        layoutInflater = LayoutInflater.from(context);

        // Инициализируем адаптер
        userListAdapter = new UserListAdapter();

        // Устанавливаем адаптер
        listView.setAdapter(userListAdapter);
    }

    // Внутренний класс адаптера
    private class UserListAdapter extends BaseAdapter {

        /**
         * Возвращает длину списка пользователей
         * @return длина списка пользователей
         */
        @Override
        public int getCount() {
            return users.size();
        }

        /**
         * Возвращает объект из списка пользователей
         * @return объект из списка пользователей
         */
        @Override
        public User getItem(int position) {
            return users.get(position);
        }

        /**
         * Возвращает позицию объекта в списке пользователей
         * @return позиция объекта в списке пользователей
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Создаёт отображаемый элемент списка
         * @param position позиция
         * @param currentView view которое будет возвращено
         * @param parent родитель - ViewGroup
         * @return отображаемый элемент списка
         */
        @Override
        public View getView(int position, View currentView, ViewGroup parent) {
            // Пользователь из списка
            User currentUser = getItem(position);

            // "Надуваем" view разметкой "item_user"
            currentView = layoutInflater.inflate(R.layout.item_user, parent, false);

            // Получаем NameTextView из currentView
            TextView nameView = currentView.findViewById(R.id.NameTextView);

            // Получаем StateTextView из currentView
            TextView stateView = currentView.findViewById(R.id.StateTextView);

            // Устанавливаем нужный текст
            nameView.setText(currentUser.getName());
            stateView.setText(currentUser.getState());

            // Возвращаем отображаемый элемент списка
            return currentView;
        }
    }
}