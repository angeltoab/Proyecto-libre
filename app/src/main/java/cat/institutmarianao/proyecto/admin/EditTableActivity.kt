package cat.institutmarianao.proyecto.admin

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cat.institutmarianao.proyecto.R
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class EditTableActivity : AppCompatActivity() {
    private lateinit var table: Table

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_table)

        table = intent.getSerializableExtra("table") as Table

        val capacityField = findViewById<EditText>(R.id.capacityField)
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        capacityField.setText(table.capacity.toString())

        confirmButton.setOnClickListener {
            val capacity = capacityField.text.toString().toIntOrNull()

            if (capacity == null || capacity <= 0) {
                Toast.makeText(this, "Introduce una capacidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            updateTable(table.id, capacity)
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun updateTable(tableId: Int, capacity: Int) {
        val url = "http://10.0.2.2:8080/Taulas/$tableId"

        val jsonBody = JSONObject().apply {
            put("capacitat", capacity)
        }

        val request = JsonObjectRequest(
            Request.Method.PUT, url, jsonBody,
            { response ->
                Toast.makeText(this, "Mesa actualizada correctamente", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            },
            { error ->
                Toast.makeText(this, "Error al actualizar la mesa", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}