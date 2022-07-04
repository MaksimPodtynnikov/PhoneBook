package com.practic.phonebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;
    int id =-1;
    Person person;

    ImageButton avatar;
    EditText name;
    EditText family;
    EditText patronymic;
    EditText phone;
    Bitmap bitmap = null;
    Button delete;
    DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        name = findViewById(R.id.editTextTextPersonName);
        family = findViewById(R.id.editTextTextPersonFamily);
        patronymic = findViewById(R.id.editTextTextPersonPatr);
        delete = findViewById(R.id.DeleteButton);
        phone = findViewById(R.id.editTextPhone);
        avatar = findViewById(R.id.imageButton2);
        adapter = new DatabaseAdapter(this);
        adapter.open();
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            id = arguments.getInt("id");
            Person person = adapter.getPerson(id);
            adapter.close();
            name.setText(person.getName());
            family.setText(person.getFamily());
            patronymic.setText(person.getPatronymic());
            phone.setText(person.getPhone());
            avatar.setImageBitmap(person.getImage());
        }
        if(id==-1) delete.setVisibility(View.INVISIBLE);
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
        bitmap = null;
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = imageReturnedIntent.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                avatar.setImageBitmap(bitmap);
            }
        }
    }

    public void onDeleteClick(View view)
    {
        adapter.open();
        adapter.delete(id);
        adapter.close();
        goHome();
    }

    private byte[] BitmapToBlob(Bitmap bitmap)
    {
        if(bitmap!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();
            return byteArray;
        }
        else return null;
    }
    private byte[] squeeze(byte[] imagem_img){
        if(imagem_img!=null) {
            while (imagem_img.length > 500000) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imagem_img, 0, imagem_img.length);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.8), (int) (bitmap.getHeight() * 0.8), true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imagem_img = stream.toByteArray();
            }
            return imagem_img;
        }
        else return null;
    }

    public void onSaveClick(View view) {
        if(!name.getText().toString().equals("") && !family.getText().toString().equals("") &&
                !patronymic.getText().toString().equals("") && !phone.getText().toString().equals("")) {
            person = new Person(id, name.getText().toString(), family.getText().toString(),
                    patronymic.getText().toString(), phone.getText().toString(), squeeze(BitmapToBlob(bitmap)));
            adapter.open();
            if(id!=-1) adapter.update(person);
            else adapter.insert(person);
            adapter.close();
            goHome();
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
    private void goHome(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void onBackClick(View view) {
        goHome();
    }

    public void onCallClick(View view)
    {
        String toDial="tel:+7"+phone.getText().toString();
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(toDial)));
    }

}