package com.example.android.gcekfb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.gcekfb.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterStep2Activity extends AppCompatActivity {

    TextView mNameTextView, mBranchTextView, mBatchTextView;
    EditText mEmailEditText, mPasswordEditText, mConfirmPasswordEditText;
    Button mRegisterBtn;

    String username, email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

        mNameTextView = (TextView) findViewById(R.id.name_text_view);
        mBranchTextView = (TextView) findViewById(R.id.branch_text_view);
        mBatchTextView = (TextView) findViewById(R.id.batch_text_view);

        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mConfirmPasswordEditText = (EditText) findViewById(R.id.confirm_password_edit_text);

        mRegisterBtn = (Button) findViewById(R.id.register_btn);

        username = (String) getIntent().getStringExtra("username");
        String name = (String) getIntent().getStringExtra("name");
        String branch = (String) getIntent().getStringExtra("branch");
        String batch = (String) getIntent().getStringExtra("batch");

        mNameTextView.setText(name);
        mBranchTextView.setText(branch);
        mBatchTextView.setText(batch);


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmailEditText.getText().toString().trim();
                password = mPasswordEditText.getText().toString().trim();
                confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

                if(email.isEmpty()) {
                    mEmailEditText.setError("Email is required");
                    return;
                }

                if(password.isEmpty()) {
                    mPasswordEditText.setError("Password is required");
                    return;
                }

                if(confirmPassword.isEmpty()) {
                    mConfirmPasswordEditText.setError("Confirm Password is required");
                    return;
                }

                if(!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterStep2Activity.this, "Password didnt match", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    postDataToServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    void postDataToServer() throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder =
                HttpUrl.parse(NetworkUtils.API_URL + "/user/create").newBuilder();


        String url = urlBuilder.build().toString();

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("email", email)
                .add("password", password)
                .add("confirmPassword", confirmPassword)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Hello", e.getMessage());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.i("RESPONSE", response.body().string());

            }
        });
    }
}
