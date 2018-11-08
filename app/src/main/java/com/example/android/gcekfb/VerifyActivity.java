package com.example.android.gcekfb;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.gcekfb.utilities.NetworkUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VerifyActivity extends AppCompatActivity {

    private EditText mVerifyEditText;
    private Button mRegisterButton;
    private ProgressBar mProgressBar;
    private LinearLayout mMainArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        mVerifyEditText = (EditText) findViewById(R.id.verify_code_edit_text);
        mRegisterButton = (Button) findViewById(R.id.register_btn);
        mMainArea = (LinearLayout) findViewById(R.id.main_area);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationCode = mVerifyEditText.getText().toString().trim();

                try {
                    verifyEmailWithVerificationCode(verificationCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void verifyEmailWithVerificationCode(String verificationCode) throws IOException {
        showProgressBar();

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl
                .parse(NetworkUtils.API_URL + "/user/verify")
                .newBuilder();

        String url = urlBuilder.build().toString();

        RequestBody formBody = new FormBody.Builder()
                .add("verify_code", verificationCode)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Context context = VerifyActivity.this;
                String msg = "Failed to verify you code";
                Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                hideProgressBar();
                Log.i("Response", response.body().string());
            }
        });

    }

    void showProgressBar() {
        mMainArea.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressBar() {
        mMainArea.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
