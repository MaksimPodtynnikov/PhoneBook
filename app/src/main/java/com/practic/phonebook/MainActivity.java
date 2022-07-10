package com.practic.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static final int PICKFILE_RESULT_CODE = 1;
    static final int PICKDIR_RESULT_CODE = 2;
    ArrayList<Person> persons;
    ListView contacts;
    PersonAdapter personsAdapter;
    EditText search;
    DatabaseAdapter adapter;
    private final int REQUEST_CODE_PERMISSION_WRITE=101;
    private final int REQUEST_CODE_PERMISSION_READ=102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, EditActivity.class);
        contacts = findViewById(R.id.contacts);
        search = findViewById(R.id.editTextSearch);
        adapter = new DatabaseAdapter(this);
        AdapterView.OnItemClickListener itemListener = (parent, v, position, id) -> {
            Person selectedPerson = (Person) parent.getItemAtPosition(position);
            intent.putExtra("id",selectedPerson.getId());
            startActivity(intent);
        };
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_DENIED)  {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE);
        }
        permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_DENIED)  {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_READ);
        }
        contacts.setOnItemClickListener(itemListener);
        // установка слушателя изменения текста
        persons = new ArrayList<>();
        personsAdapter = new PersonAdapter(this, R.layout.list_item, persons);
        contacts.setAdapter(personsAdapter);
        search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            // при изменении текста выполняем фильтрацию
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.open();
                persons.clear();
                if(s == null || s.length() == 0) persons.addAll(adapter.getPersons());
                else persons.addAll(adapter.search(s.toString()));
                adapter.close();
            }
        });
    }
    public void exportList(View view)
    {
        Intent intent = new Intent(this, OpenDirActivity.class);
        startActivityForResult(intent, PICKDIR_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent ReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, ReturnedIntent);
        if (requestCode == PICKFILE_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = ReturnedIntent.getData();
                adapter.open();
                for (Person person: adapter.getPersons()) {
                    adapter.delete(person.getId());
                }
                for (Person person: fileWorker.importPersons(fileWorker.getPathFromUri(this,uri))) {
                    adapter.insert(person);
                }
                adapter.close();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Контакты загружены", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if(requestCode == PICKDIR_RESULT_CODE)
        {
            if (ReturnedIntent == null) {return;}
            String url = ReturnedIntent.getStringExtra("url");
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "/contacts-"+ n +".lst";
            File file = new File (url, fname);
            fileWorker.exportPersons(persons,file.getAbsolutePath());
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Файл contacts.lst сохранен!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void importList(View view)
    {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Выберите список контактов");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        adapter.open();
        persons.clear();
        persons.addAll(adapter.getPersons());
        adapter.close();
        personsAdapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public void fullContactsClick(View view)
    {
        String[] names = {"Иван", "Сергей", "Дмитрий", "Аркадий"};
        String[] families = {"Иванов", "Пеньков", "Синьков", "Глушков"};
        String[] patronymics = {"Максимович","Павлович","Романович","Степанович"};
        String[] phones = {"98657456787","95635479876","9806548798","94536547878"};
        adapter.open();
        for (int i=0;i<30;i++) {
            int nameRand = (int)(Math.random()*(3+1));
            int famRand = (int)(Math.random()*(3+1));
            int patrRand = (int)(Math.random()*(3+1));
            int phoneRand = (int)(Math.random()*(3+1));
            adapter.insert(new Person(1,names[nameRand],families[famRand],
                    patronymics[patrRand],phones[phoneRand],null));
        }
        persons.clear();
        persons.addAll(adapter.getPersons());
        adapter.close();
        personsAdapter.notifyDataSetChanged();
    }

}