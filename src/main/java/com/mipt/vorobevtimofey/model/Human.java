package com.mipt.vorobevtimofey.model;

public class Human {
    private String name;
    private String surname;
    private int age;
    private boolean working;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public boolean getWorking() {
        return working;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBrand(boolean working) {
        this.working = working;
    }

}
