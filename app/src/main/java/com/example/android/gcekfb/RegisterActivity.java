package com.example.android.gcekfb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextView responseBody;

    private EditText admnoInput;

    private Button regsterBtn;

    private CardView errorCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        admnoInput = (EditText) findViewById(R.id.admno_edit_text);
        regsterBtn = (Button) findViewById(R.id.register_btn);
        errorCardView = (CardView) findViewById(R.id.error_card_view);

        regsterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String admno = RegisterActivity.this.admnoInput.getText().toString();
                Toast.makeText(RegisterActivity.this, admno, Toast.LENGTH_SHORT).show();
                try {
                    getStudentInfo(admno);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void getStudentInfo(String admno) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder =
                HttpUrl.parse(NetworkUtils.API_URL + "/student/get").newBuilder();
        urlBuilder.addQueryParameter("admno", admno);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Hello", e.getMessage());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsonRes = response.body().string();

                Log.i("response", jsonRes);

                JSONObject jsonObject = null;
                Bundle bundle = new Bundle();


                try {
                    jsonObject = new JSONObject(jsonRes);


                    if(jsonObject.has("status") &&
                            jsonObject.getString("status").equals("failed")) {

                        RegisterActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                errorCardView.setVisibility(View.VISIBLE);
                            }
                        });
                        return;
                    }

                    JSONObject data = jsonObject.getJSONObject("data");


                    Intent startRegisterStep2Activity =
                            new Intent(RegisterActivity.this, RegisterStep2Activity.class);

                    startRegisterStep2Activity.putExtra("username", data.getString("admission_no"));
                    startRegisterStep2Activity.putExtra("name", data.getString("name"));
                    startRegisterStep2Activity.putExtra("branch", data.getString("branch"));
                    startRegisterStep2Activity.putExtra("batch", data.getString("batch"));


                    startActivity(startRegisterStep2Activity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
