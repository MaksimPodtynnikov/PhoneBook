package com.practic.phonebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class EditActivity extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;
    int pos =-1;
    Person person;

    ImageButton avatar;
    EditText name;
    EditText family;
    EditText patronymic;
    EditText phone;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        name = findViewById(R.id.editTextTextPersonName);
        family = findViewById(R.id.editTextTextPersonFamily);
        patronymic = findViewById(R.id.editTextTextPersonPatr);
        phone = findViewById(R.id.editTextPhone);
        avatar = findViewById(R.id.imageButton2);
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            pos = arguments.getInt("position");
            if(arguments.containsKey("person")) {
                person = (Person) arguments.getSerializable("person");
                name.setText(person.name);
                family.setText(person.family);
                patronymic.setText(person.patronymic);
                phone.setText(person.phone);
                try {
                    avatar.setImageBitmap(BitmapFactory.decodeStream(this.openFileInput(person.image)));
                    path = person.image;
                } catch (Exception ignored) {
                }
            }
        }
    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage"+pos;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public void onAvatarClick(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Bitmap bitmap = null;
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = imageReturnedIntent.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                path = createImageFromBitmap(bitmap);
                avatar.setImageBitmap(bitmap);
            }
        }
    }


    public void onSaveClick(View view) {
        if(!name.getText().toString().equals("") && !family.getText().toString().equals("") &&
                !patronymic.getText().toString().equals("") && !phone.getText().toString().equals("")) {
            Intent intent = new Intent();
            person = new Person(name.getText().toString(), family.getText().toString(),
                    patronymic.getText().toString(), phone.getText().toString(),path);
            intent.putExtra("person", person);
            intent.putExtra("position", pos);
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Предупреждение!")
                    .setMessage("Вы не заполнили данные!")
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.create();
            builder.show();
        }
    }

    public void onBackClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}