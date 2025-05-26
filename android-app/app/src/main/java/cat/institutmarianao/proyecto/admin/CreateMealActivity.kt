package cat.institutmarianao.proyecto.admin

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cat.institutmarianao.proyecto.R
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import cat.institutmarianao.proyecto.admin.Meal
class CreateMealActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meal)

        val nameField = findViewById<EditText>(R.id.nameField)
        val descriptionField = findViewById<EditText>(R.id.descriptionField)
        val categorySpinner = findViewById<Spinner>(R.id.categorySpinner)
        val priceField = findViewById<EditText>(R.id.priceField)
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        val categories = listOf("Entrant", "Principal", "Postre", "Beguda")
        categorySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)

        confirmButton.setOnClickListener {
            val name = nameField.text.toString()
            val description = descriptionField.text.toString()
            val category = categorySpinner.selectedItem.toString()

            val rawPrice = priceField.text.toString()
                .replace(",", ".")          // permite coma como separador decimal
                .trimStart('0')             // elimina ceros al inicio
                .ifEmpty { "0" }            // evita vacío si solo había ceros

            val price = rawPrice.toDoubleOrNull()

            if (name.isBlank() || description.isBlank() || price == null) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createMeal(name, description, category, price)
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun createMeal(name: String, description: String, category: String, price: Double) {
        val url = "http://10.0.2.2:8080/plats/createPlat"

        val jsonBody = JSONObject().apply {
            put("nom", name)
            put("descripcio", description)
            put("categoria", category)
            put("preu", price)
            put("recomanacio", "No")
            put("puntuacio", 0.0)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                Toast.makeText(this, "Plat creat correctament", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            },
            { error ->
                Toast.makeText(this, "Error al crear el plat", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}
