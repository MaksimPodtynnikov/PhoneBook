package com.practic.phonebook;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.Serializable;

public class Person implements Serializable {
    String name;
    String family;
    String patronymic;
    String image;
    String phone;

    public Person(String name, String family, String patronymic, String phone, String image)
    {
        this.name = name;
        this.family = family;
        this.patronymic = patronymic;
        this.phone = phone;
        this.image = image;
    }
}
