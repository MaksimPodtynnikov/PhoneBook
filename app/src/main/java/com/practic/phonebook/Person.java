package com.practic.phonebook;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;

public class Person implements Serializable {
    private int id;
    private String name;
    private String family;
    private String patronymic;
    private byte[] image;
    private String phone;

    public Person(int id, String name, String family, String patronymic, String phone, byte[] image)
    {
        this.id = id;
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
    public int getId()
    {
        return id;
    }

    public Bitmap getImage() {
        if(image!=null)
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        else return null;
    }
    public byte[] getImageBytes()
    {
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
    public void setId(int Id) {
        this.id = Id;
    }

    public void setImage(byte[] image) {
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
