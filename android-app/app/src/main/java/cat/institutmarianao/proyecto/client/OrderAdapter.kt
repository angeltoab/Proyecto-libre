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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

class OrderAdapter(context: Context, private val meals: List<Meal>) : ArrayAdapter<Meal>(context, 0, meals) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_client_orders, parent, false)
        }

        val currentMeal = meals[position]

        val mealImage = listItemView?.findViewById<ImageView>(R.id.mealImage)
        val mealName = listItemView?.findViewById<TextView>(R.id.mealName)
        val mealDescription = listItemView?.findViewById<TextView>(R.id.mealDescription)
        val mealCategory = listItemView?.findViewById<TextView>(R.id.mealCategory)
        val mealPrice = listItemView?.findViewById<TextView>(R.id.mealPrice)
        val button = listItemView?.findViewById<Button>(R.id.button)

        val stars = listOf(
            listItemView?.findViewById<ImageView>(R.id.star1),
            listItemView?.findViewById<ImageView>(R.id.star2),
            listItemView?.findViewById<ImageView>(R.id.star3),
            listItemView?.findViewById<ImageView>(R.id.star4),
            listItemView?.findViewById<ImageView>(R.id.star5)
        )

        mealName?.text = currentMeal.name
        mealDescription?.text = currentMeal.description
        mealCategory?.text = currentMeal.category

        // Formatear el precio
        val formattedPrice = NumberFormat.getNumberInstance(Locale("es", "ES")).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }.format(currentMeal.price)
        mealPrice?.text = "$formattedPrice€"

        // Mostrar las estrellas según la puntuación
        val score = currentMeal.score
        stars.forEachIndexed { index, imageView ->
            when {
                index < score.toInt() -> imageView?.setImageResource(R.drawable.ic_star_full)
                index == score.toInt() && score - score.toInt() >= 0.5 -> imageView?.setImageResource(R.drawable.ic_star_half)
                else -> imageView?.setImageResource(R.drawable.ic_star_empty)
            }
        }

        // Configurar el botón para crear pedidos
        button?.setOnClickListener {
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
                Request.Method.POST, url, jsonBody,
                { response ->
                    Toast.makeText(context, "Comanda enviada correctament", Toast.LENGTH_SHORT).show()
                },
                { error ->
                    error.printStackTrace()
                    Toast.makeText(context, "Error al crear la comanda", Toast.LENGTH_SHORT).show()
                }
            ) {}

            queue.add(request)
        }


        return listItemView!!
    }
}
