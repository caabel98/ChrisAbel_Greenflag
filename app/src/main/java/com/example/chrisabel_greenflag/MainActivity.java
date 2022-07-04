package com.example.chrisabel_greenflag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    //Global Variables
    ImageButton ibCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ibCreateAccount = findViewById(R.id.ib_create_account);
        ibCreateAccount.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreateAnAccountActivity.class);
            startActivity(intent);
        });
    }
}