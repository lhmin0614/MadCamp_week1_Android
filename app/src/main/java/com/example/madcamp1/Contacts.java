package com.example.madcamp1;

public class Contacts implements Comparable<Contacts>{
    private String name;
    private String photo;
    private String phone_num;

    public Contacts(String name, String phone_num, String photo) {
        this.name = name;
        this.photo = photo;
        this.phone_num = phone_num;
    }
    @Override
    public int compareTo (Contacts c) {
        int compareResult = this.name.compareTo(c.name);
        if (compareResult < 0) {
            return -1;
        } else if (compareResult > 0) {
            return 1;
        }
        return 0;
    }

    public String getName() { return name; }

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
