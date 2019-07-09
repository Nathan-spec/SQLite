package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSave, btnSearch, btnDelete, btnSelectAll, btnUpdate;
    SQLiteDatabase sqLiteDatabase;
    EditText Search, Name, Email, Age;
    String name, mail, age, search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteDatabase = openOrCreateDatabase("My Db",
                Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF  NOT EXISTS students (ID INTEGER PRIMARY KEY AUTOINCREMENT , Name VARCHAR (255),Email VARCHAR(255),Age VARCHAR(255))");
        setContentView(R.layout.activity_main);
        btnSave = findViewById(R.id.btnSave);
        btnSearch = findViewById(R.id.btnSearch);
        btnDelete = findViewById(R.id.btnDelete);
        btnSelectAll = findViewById(R.id.btnSelectAll);
        btnUpdate = findViewById(R.id.btnUpdate);
        Search = findViewById(R.id.Search);
        Name = findViewById(R.id.Name);
        Email = findViewById(R.id.Email);
        Age = findViewById(R.id.Age);
        btnSave.setOnClickListener(this);
        btnSelectAll.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {
            name = Name.getText().toString().trim();
            mail = Email.getText().toString().trim();
            age = Age.getText().toString().trim();
            if (name.equals(" ") || mail.equals(" ") || age.equals(" ")) {
                Toast.makeText(this, "Input is Required in various fields..", Toast.LENGTH_SHORT).show();
                return;
            } else {
                sqLiteDatabase.execSQL("Insert into students(Name,Email,Age) VALUES(' " + name + " ',' " + mail + " ' , ' " + age + " ' );");
                Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btnSelectAll) {
            Cursor c = sqLiteDatabase.rawQuery(" Select * from students", null);
            if (c.getCount() == 0) {
                Toast.makeText(this, "Database Empty ", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {
                buffer.append("Student Name :" + c.getString(1) + "\n");
                buffer.append("Email :" + c.getString(2) + "\n");
                buffer.append("Age :" + c.getString(3) + "\n");
            }
            Toast.makeText(this, buffer.toString(), Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btnSearch) {
            search = Search.getText().toString().trim();
            if (search.equals("")) {
                Toast.makeText(this, "Enter Student Name first", Toast.LENGTH_SHORT).show();
                return;
            }
            Cursor c = sqLiteDatabase.rawQuery(" Select * from students Where Name= ' " + btnSearch + " '", null);
            if (c.moveToFirst()) {
                Name.setText(c.getString(1));
                Email.setText(c.getString(2));
                Age.setText(c.getString(3));
            }
        } else if (v.getId() == R.id.btnUpdate) {
            search = Search.getText().toString().trim();
            name = Name.getText().toString().trim();
            mail = Email.getText().toString().trim();
            age = Age.getText().toString().trim();
            if (search.equals("")) {
                Toast.makeText(this, "Enter Students name to update", Toast.LENGTH_SHORT).show();
                return;
            }
            Cursor cursorupdate = sqLiteDatabase.rawQuery(" Select * from students Where Name= ' " + btnSearch + " '", null);

            if (cursorupdate.moveToFirst()) {
                if (name.equals(" ") || age.equals(" ")) {
                    Toast.makeText(this, "Fields cant be empty ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    sqLiteDatabase.execSQL("Update students Set Name = ' " + name + " ',Email = ' " + mail + "' ,Age = ' " + age + " ' ");
                }
            } else if (v.getId() == R.id.btnDelete) {
                search = Search.getText().toString().trim();
                if (search.equals("")) {
                    Toast.makeText(this, "Enter Student Name first", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor cdelete = sqLiteDatabase.rawQuery(" Select * from students Where Name= ' " + btnSearch + " '", null);
                if (cdelete.moveToFirst()) {
                    sqLiteDatabase.execSQL("Delete from students where Name ='" +btnSearch+ " ' " );
                    Toast.makeText(this, " Record Deleted", Toast.LENGTH_SHORT).show();

                }
                    Toast.makeText(this, "student not found", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
