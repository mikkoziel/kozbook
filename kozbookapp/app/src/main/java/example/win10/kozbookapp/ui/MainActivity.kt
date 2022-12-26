package example.win10.kozbookapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import example.win10.kozbookapp.R

class MainActivity : AppCompatActivity() {
    var txtString: TextView? = null
    var url = "https://reqres.in/api/users/2"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtString = findViewById(R.id.txtString)

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

    fun changeButton(view: View) {
        val myIntent = Intent(this, LibraryActivity::class.java)
        startActivity(myIntent)}
}