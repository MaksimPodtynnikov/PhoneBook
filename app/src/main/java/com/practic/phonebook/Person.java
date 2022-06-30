package com.practic.phonebook;


import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private String family;
    private String patronymic;
    private String image;
    private String phone;

    public Person(String name, String family, String patronymic, String phone, String image)
    {
        this.name = name;
        this.family = family;
        this.patronymic = patronymic;
        this.phone = phone;
        this.image = image;
    }
    public String getName()
    {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getFamily() {
        return family;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
