package com.example.teh_k.ChoreMate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class ForgotPasswordActivity extends AppCompatActivity {

    // The appbar on the screen.
    private Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Creates the appbar.
        appbar = findViewById(R.id.appbar_forgot_password);
        setSupportActionBar(appbar);
    }
}
