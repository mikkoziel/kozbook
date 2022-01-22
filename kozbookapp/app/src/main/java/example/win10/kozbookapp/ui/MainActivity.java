package example.win10.kozbookapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import example.win10.kozbookapp.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView txtString;
    public String url= "https://reqres.in/api/users/2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtString= findViewById(R.id.txtString);

//        try {
//            run();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    void run() throws IOException {
//
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                call.cancel();
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//
//                assert response.body() != null;
//                final String myResponse = response.body().string();
//
//                MainActivity.this.runOnUiThread(() -> txtString.setText(myResponse));
//
//            }
//        });
//    }

    public void change_button(View v){
        Intent myIntent = new Intent(this, LibraryActivity.class);
        startActivity(myIntent);
    }

}