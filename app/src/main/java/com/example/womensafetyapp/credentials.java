package com.example.womensafetyapp;

public class credentials {
    String fullname, age, gender, number, password, contacts;

    public credentials(){
    }

    public credentials(String fullname, String age, String gender, String number, String password) {
        this.fullname = fullname;
        this.age = age;
        this.gender = gender;
        this.number = number;
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
