package cat.institutmarianao.proyecto.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import cat.institutmarianao.proyecto.R
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import cat.institutmarianao.proyecto.admin.Meal
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class MealsManagementActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: MealAdapter
    private val meals = mutableListOf<Meal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meals_management)

        // 1. Inicializar vistas
        setupToolbar()
        listView = findViewById(R.id.mealsListView)

        // 2. Inicializar el adapter ANTES de usarlo
        setupAdapter()

        // 3. Configurar el adapter en la lista
        listView.adapter = adapter

        // 4. Cargar datos
        loadMeals()

        // Resto de la configuración
        val createButton: Button = findViewById(R.id.createButton)
        createButton.setOnClickListener {
            val intent = Intent(this, CreateMealActivity::class.java)
            startActivityForResult(intent, 1001)
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val meal = meals[position]
            showOptionsDialog(meal)
            true
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1001 || requestCode == 1002) && resultCode == Activity.RESULT_OK) {
            loadMeals()
        }
    }

    private fun showOptionsDialog(meal: Meal) {
        val options = arrayOf("Editar", "Eliminar")

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Opciones para ${meal.name}")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> editMeal(meal) // Editar
                1 -> showDeleteConfirmationDialog(meal) // Eliminar
            }
        }
        builder.show()
    }

    private fun editMeal(meal: Meal) {
        val intent = Intent(this, EditMealActivity::class.java).apply {
            putExtra("meal", meal)
        }
        startActivityForResult(intent, 1002)
    }
    private fun setupToolbar() {
        val myToolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolbar)
        supportActionBar?.title = ""
        val buttonContainer: FrameLayout = findViewById(R.id.toolbar_button_container)
        buttonContainer.visibility = View.VISIBLE

        val goBackButton: ImageButton = findViewById(R.id.go_back_button)
        goBackButton.setOnClickListener {
            finish()
        }
    }
    private fun loadMeals() {
        val url = "http://10.0.2.2:8080/plats"

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response: JSONArray ->
                meals.clear()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    val meal = Meal(
                        id_plat = item.getInt("id_plat"),
                        name = item.getString("nom"),
                        description = item.getString("descripcio"),
                        category = item.getString("categoria"),
                        recommendation = item.getString("recomanacio"),
                        price = item.getDouble("preu"),
                        score = item.getDouble("puntuacio")
                    )
                    meals.add(meal)
                }

                // Ordenar la lista por id_plat descendente (mayor a menor)
                meals.sortByDescending { it.id_plat }

                adapter.notifyDataSetChanged()
            },
            { error ->
                error.printStackTrace()
            })

        Volley.newRequestQueue(this).add(request)
    }
    private fun showDeleteConfirmationDialog(meal: Meal) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Eliminar plat")
        builder.setMessage("Estàs segur que vols eliminar el plat '${meal.name}'?")
        builder.setPositiveButton("Sí") { _, _ ->
            deleteMeal(meal)
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }
    private fun deleteMeal(meal: Meal) {
        val url = "http://10.0.2.2:8080/plats/${meal.id_plat}"

        val request = com.android.volley.toolbox.StringRequest(
            com.android.volley.Request.Method.DELETE, url,
            {
                Toast.makeText(this, "Plat eliminat correctament", Toast.LENGTH_SHORT).show()
                loadMeals() // Recarga la lista tras eliminar
            },
            { error ->
                Toast.makeText(this, "Error al eliminar el plat", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            })

        Volley.newRequestQueue(this).add(request)
    }


    private fun setupAdapter() {
        adapter = MealAdapter(this, meals) { meal, isRecommended ->
            updateRecommendation(meal, isRecommended)
        }
        listView.adapter = adapter
    }
    private fun updateRecommendation(meal: Meal, isRecommended: Boolean) {
        val url = "http://10.0.2.2:8080/plats/updateRecommendation/${meal.id_plat}"
        val recommendation = if (isRecommended) "Si" else "No"

        val jsonBody = JSONObject().apply {
            put("recomanacio", recommendation)
        }

        val request = JsonObjectRequest(
            Request.Method.PUT, url, jsonBody,
            { response ->
                // Actualizar localmente el estado de recomendación
                meals.find { it.id_plat == meal.id_plat }?.recommendation = recommendation
                Toast.makeText(this, "Recomanació actualitzada", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(this, "Error al actualitzar la recomanació", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
                // Revertir el cambio en la UI
                adapter.notifyDataSetChanged()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}