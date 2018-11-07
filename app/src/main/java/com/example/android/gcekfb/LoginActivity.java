package com.example.android.gcekfb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button createAccntBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAccntBtn = (Button) findViewById(R.id.create_acct_btn);

        createAccntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(startRegisterActivity);
            }
        });
    }
}
