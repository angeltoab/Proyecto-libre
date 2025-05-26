package cat.institutmarianao.proyecto.admin

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cat.institutmarianao.proyecto.R
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.Serializable

class EditMealActivity : AppCompatActivity() {

    private lateinit var meal: Meal

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_meal)

        meal = intent.getSerializableExtra("meal") as Meal

        val nameField = findViewById<EditText>(R.id.nameField)
        val descriptionField = findViewById<EditText>(R.id.descriptionField)
        val categorySpinner = findViewById<Spinner>(R.id.categorySpinner)
        val priceField = findViewById<EditText>(R.id.priceField)
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        // Cargar los datos del plato en los campos
        nameField.setText(meal.name)
        descriptionField.setText(meal.description)
        priceField.setText(meal.price.toString())

        val categories = listOf("Entrant", "Principal", "Postre", "Beguda")
        categorySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)

        // Seleccionar la categoría actual
        val categoryPosition = categories.indexOf(meal.category)
        if (categoryPosition != -1) {
            categorySpinner.setSelection(categoryPosition)
        }

        confirmButton.setOnClickListener {
            val name = nameField.text.toString()
            val description = descriptionField.text.toString()
            val category = categorySpinner.selectedItem.toString()

            val rawPrice = priceField.text.toString()
                .replace(",", ".")
                .trimStart('0')
                .ifEmpty { "0" }

            val price = rawPrice.toDoubleOrNull()

            if (name.isBlank() || description.isBlank() || price == null) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            updateMeal(meal.id_plat, name, description, category, price)
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun updateMeal(mealId: Int, name: String, description: String, category: String, price: Double) {
        val url = "http://10.0.2.2:8080/plats/updatePlat/$mealId"

        val jsonBody = JSONObject().apply {
            put("nom", name)
            put("descripcio", description)
            put("categoria", category)
            put("preu", price)
            put("recomanacio", meal.recommendation)
            put("puntuacio", meal.score)
        }

        val request = JsonObjectRequest(
            Request.Method.PUT, url, jsonBody,
            { response ->
                Toast.makeText(this, "Plat actualitzat correctament", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            },
            { error ->
                Toast.makeText(this, "Error al actualitzar el plat", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}