package com.example.pz19;

public class User {
    private String Name;
    private String State;
    private int Age;
    private int StateSignal;
    private int id;
    private static int nextId = 0;

    public User(String name, String state, int age, int stateSignal) {
        Name = name;
        State = state;
        Age = age;
        StateSignal = stateSignal;
        this.id = nextId++;
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

    public int getStateSignal() {
        return StateSignal;
    }

    public void setStateSignal(int stateSignal) {
        StateSignal = stateSignal;
    }

    public int getId() {
        return id;
    }
}