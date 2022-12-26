package example.win10.kozbookapp.service

import okhttp3.*
import java.io.IOException
import kotlin.Throws

class HttpConnection {
    private val client: OkHttpClient = OkHttpClient()

    fun makeRequest(url: String) {
        val request = Request.Builder()
                .url(url)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                assert(response.body() != null)
                val myResponse = response.body()!!.string()

//                MainActivity.this.runOnUiThread(() -> txtString.setText(myResponse));
            }
        })
    }
}