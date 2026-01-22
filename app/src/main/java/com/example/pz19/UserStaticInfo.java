package com.example.pz19;

import java.util.ArrayList;
import java.util.List;

public class UserStaticInfo {

    public static List<User> users = new ArrayList<>();

    public static final String POSITION = "position";

    public UserStaticInfo() {
        if (users.isEmpty()) {
            addDefaultUsers();
        }
    }

    private void addDefaultUsers() {
        users.add(new User("Иван", "В сети", 19, 1));
        users.add(new User("Мария", "Не в сети", 25, 0));
        users.add(new User("Алексей", "В сети", 22, 1));
        users.add(new User("Ольга", "Занят", 30, 2));
        users.add(new User("Дмитрий", "В сети", 28, 1));
        users.add(new User("Анна", "Отошёл", 24, 2));
        users.add(new User("Сергей", "Не в сети", 31, 0));
        users.add(new User("Елена", "В сети", 27, 1));
    }

    public static void updateUser(int position, User updatedUser) {
        if (position >= 0 && position < users.size()) {
            users.set(position, updatedUser);
        }
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static void removeUser(int position) {
        if (position >= 0 && position < users.size()) {
            users.remove(position);
        }
    }
}