package cat.institutmarianao.proyecto.client

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cat.institutmarianao.proyecto.R
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

data class Meal(
    val id_plat: Int,
    val name: String,
    val description: String,
    val category: String,
    val recommendation: String?,
    val price: Double,
    val score: Double // Cambiar a Double para manejar decimales
)

class MealAdapter(context: Context, meals: List<Meal>) :
    ArrayAdapter<Meal>(context, 0, meals) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_client, parent, false)

        val meal = getItem(position)

        val mealName = view.findViewById<TextView>(R.id.mealName)
        val mealDescription = view.findViewById<TextView>(R.id.mealDescription)
        val mealCategory = view.findViewById<TextView>(R.id.mealCategory)
        val mealPrice = view.findViewById<TextView>(R.id.mealPrice)
        val buttonSend = view.findViewById<Button>(R.id.button)

        val stars = listOf(
            view.findViewById<ImageView>(R.id.star1),
            view.findViewById<ImageView>(R.id.star2),
            view.findViewById<ImageView>(R.id.star3),
            view.findViewById<ImageView>(R.id.star4),
            view.findViewById<ImageView>(R.id.star5)
        )

        meal?.let { currentMeal ->
            mealName.text = currentMeal.name
            mealDescription.text = currentMeal.description
            mealCategory.text = currentMeal.category
            val formattedPrice = NumberFormat.getNumberInstance(Locale("es", "ES")).apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 2
            }.format(currentMeal.price)

            mealPrice.text = "$formattedPrice€"

            val score = currentMeal.score

            stars.forEachIndexed { index, imageView ->
                when {
                    index < score.toInt() -> imageView.setImageResource(R.drawable.ic_star_full)
                    index == score.toInt() && score - score.toInt() >= 0.5 -> imageView.setImageResource(R.drawable.ic_star_half)
                    else -> imageView.setImageResource(R.drawable.ic_star_empty)
                }
            }

            buttonSend.setOnClickListener {
                val url = "http://10.0.2.2:8080/comandes"
                val queue = Volley.newRequestQueue(context)

                val prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                val idTaula = prefs.getInt("numero_taula", 0)
                val idUsuari = prefs.getInt("id_usuari", 1)

                val jsonBody = JSONObject().apply {
                    put("id_plat", currentMeal.id_plat)
                    put("id_taula", idTaula)
                    put("id_usuari", idUsuari)
                    put("estat", "ACTIVA")
                }


                val request = object : JsonObjectRequest(
                    Method.POST, url, jsonBody,
                    Response.Listener { response ->
                        Toast.makeText(context, "Comanda enviada correctament", Toast.LENGTH_SHORT).show()
                    },
                    Response.ErrorListener { error ->
                        error.printStackTrace()
                        Toast.makeText(context, "Error al crear la comanda", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json; charset=utf-8"
                        return headers
                    }
                }

                queue.add(request)
            }


        }

        return view
    }
}
