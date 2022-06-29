package com.practic.phonebook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Person> persons = new ArrayList<>();
    ListView contacts;
    PersonAdapter personsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, EditActivity.class);


        contacts = findViewById(R.id.contacts);
        personsAdapter = new PersonAdapter(this, R.layout.list_item, persons);
        contacts.setAdapter(personsAdapter);
        AdapterView.OnItemClickListener itemListener = (parent, v, position, id) -> {
            // получаем выбранный пункт
            Person selectedPerson = (Person) parent.getItemAtPosition(position);
            intent.putExtra("person",selectedPerson);
            intent.putExtra("position",position);
            mStartForResult.launch(intent);
        };
        contacts.setOnItemClickListener(itemListener);
        personsAdapter.notifyDataSetChanged();
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        Bundle arguments = intent.getExtras();
                        if(arguments!= null)
                        {
                            Person person = (Person) arguments.getSerializable("person");
                            if(arguments.getInt("position")<persons.size())
                                persons.set(arguments.getInt("position"),person);
                            else
                                persons.add(person);
                            personsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });


    public void onClick(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("position",persons.size());
        mStartForResult.launch(intent);
    }

}