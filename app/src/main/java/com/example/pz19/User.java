package com.example.pz19;

public class User {
    private String Name;
    private String State;
    private int Age;

    public User(String name, String state, int age) {
        Name = name;
        State = state;
        Age = age;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }
}