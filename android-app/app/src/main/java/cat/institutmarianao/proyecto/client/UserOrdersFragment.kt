package cat.institutmarianao.proyecto.client

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import cat.institutmarianao.proyecto.R
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class UserOrdersFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var adapter: OrderAdapter
    private var meals: List<Meal> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_orders, container, false)
        listView = view.findViewById(R.id.mealsListView)

        fetchUserOrders()

        return view
    }

    private fun fetchUserOrders() {
        val prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("id_usuari", -1)
        if (userId == -1) {
            Log.e("UserOrdersFragment", "User ID not found")
            return
        }

        val url = "http://10.0.2.2/projecte/read_user_orders.php?id_usuari=$userId"
        val queue = Volley.newRequestQueue(requireContext())

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                Log.d("UserOrdersFragment", "Response: $response")
                meals = parseMeals(response)
                adapter = OrderAdapter(requireContext(), meals)
                listView.adapter = adapter
            },
            { error ->
                Log.e("UserOrdersFragment", "Error fetching user orders", error)
                error.printStackTrace()
            }
        )

        queue.add(request)
    }

    private fun parseMeals(response: JSONArray): List<Meal> {
        val meals = mutableListOf<Meal>()
        for (i in 0 until response.length()) {
            val obj = response.getJSONObject(i)
            val meal = Meal(
                id_plat = obj.getInt("id_plat"),
                name = obj.getString("nom_plat"),
                description = obj.getString("descripcio"),
                category = obj.getString("categoria"),
                recommendation = obj.optString("recomanacio", ""),
                price = obj.getDouble("preu"),
                score = obj.getDouble("puntuacio") // Asegúrate de que score sea de tipo Double
            )
            meals.add(meal)
        }
        return meals
    }
}
