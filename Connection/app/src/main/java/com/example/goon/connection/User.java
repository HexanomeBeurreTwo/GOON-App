package com.example.goon.connection;

import java.util.ArrayList;

/**
 * Created by Daniela on 27/04/2016.
 */
public class User {

    private String username;
    private String password;
    private String email;
    private Integer age;
    private String citizen;
    private Integer userId;
    private String tags;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {

    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", citizen='" + citizen + '\'' +
                ", userId=" + userId +
                ", tags=" + tags +
                '}';
    }
    public User(Integer userId, String username, String password,  String email,Integer age, String citizen) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.email = email;
        this.citizen = citizen;
        this.userId = userId;

    }


    public User(Integer userId, String username, String password,  String email,Integer age, String citizen,
                String tags) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.email = email;
        this.citizen = citizen;
        this.userId = userId;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;
        if (password != null ? !password.equals(user.password) : user.password != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (age != null ? !age.equals(user.age) : user.age != null) return false;
        if (citizen != null ? !citizen.equals(user.citizen) : user.citizen != null) return false;
        if (userId != null ? !userId.equals(user.userId) : user.userId != null) return false;

        return !(tags != null ? !tags.equals(user.tags) : user.tags != null);

    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCitizen() {
        return citizen;
    }

    public void setCitizen(String citizen) {
        this.citizen = citizen;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }




}
