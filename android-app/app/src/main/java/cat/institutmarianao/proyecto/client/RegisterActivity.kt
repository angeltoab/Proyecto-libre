package cat.institutmarianao.proyecto.client

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import cat.institutmarianao.proyecto.R
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupToolbar()
        setupRegisterButton()
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

    private fun setupRegisterButton() {
        val registerButton: View = findViewById(R.id.register_button)
        registerButton.setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.email_edit_text).text.toString().trim()
            val password =
                findViewById<TextInputEditText>(R.id.password_edit_text).text.toString().trim()
            val confirmPassword =
                findViewById<TextInputEditText>(R.id.confirm_password_edit_text).text.toString()
                    .trim()
            val newsletterChecked = findViewById<CheckBox>(R.id.newsletter_checkbox).isChecked

            if (validateInput(email, password, confirmPassword)) {
                registerUser(email, password, newsletterChecked)
            }
        }
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty()) {
            findViewById<TextInputLayout>(R.id.email_input_layout).error = "El correu és obligatori"
            return false
        }
        if (password.isEmpty()) {
            findViewById<TextInputLayout>(R.id.password_input_layout).error =
                "La contrasenya és obligatòria"
            return false
        }
        if (confirmPassword.isEmpty()) {
            findViewById<TextInputLayout>(R.id.confirm_password_input_layout).error =
                "Confirma la contrasenya"
            return false
        }
        if (password != confirmPassword) {
            findViewById<TextInputLayout>(R.id.confirm_password_input_layout).error =
                "Les contrasenyes no coincideixen"
            return false
        }
        return true
    }

    private fun registerUser(email: String, password: String, newsletterChecked: Boolean) {
        val url = "http://10.0.2.2:8080/Usuaris"  // Ajusta el puerto según tu configuración Spring Boot

        val requestQueue = Volley.newRequestQueue(this)

        val jsonBody = JSONObject().apply {
            put("correu", email)
            put("contrasenyaHash", password)
            put("newsletter", if (newsletterChecked) "Si" else "No")
            put("punts", 0)
        }

        val jsonRequest = object : JsonObjectRequest(
            Method.POST, url, jsonBody,
            Response.Listener { response ->
                Toast.makeText(this, "Usuari registrat correctament", Toast.LENGTH_SHORT).show()
                // Redirigir si lo deseas:
                // startActivity(Intent(this, LoginActivity::class.java))
                // finish()
            },
            Response.ErrorListener { error ->
                val networkResponse = error.networkResponse
                val errorMessage = if (networkResponse != null && networkResponse.statusCode == 409) {
                    "El correu electrònic ja està registrat"
                } else {
                    "Error al registrar l'usuari: ${error.message}"
                }
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        requestQueue.add(jsonRequest)
    }

}
