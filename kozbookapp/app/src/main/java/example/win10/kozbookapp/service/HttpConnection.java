package example.win10.kozbookapp.service;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpConnection {
    private OkHttpClient client;

    public HttpConnection(){
        this.client = new OkHttpClient();
    }

    public void make_request(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                assert response.body() != null;
                final String myResponse = response.body().string();

//                MainActivity.this.runOnUiThread(() -> txtString.setText(myResponse));

            }
        });
    }

}
