package com.example.madcamp1;

public class Contacts {
    private String name;
    private String photo;
    private String phone_num;

    public Contacts(String name, String phone_num, String photo) {
        this.name = name;
        this.photo = photo;
        this.phone_num = phone_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }
}
