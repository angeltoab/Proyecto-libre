package cat.institutmarianao.proyecto

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class NetworkService(private val context: Context) {

    fun fetchMeals(url: String, responseListener: (JSONArray) -> Unit, errorListener: (VolleyError) -> Unit) {
        val queue = Volley.newRequestQueue(context)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response -> responseListener(response) },
            { error -> errorListener(error) }
        )

        queue.add(jsonArrayRequest)
    }

}
