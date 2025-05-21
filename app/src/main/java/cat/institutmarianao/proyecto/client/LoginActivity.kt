package cat.institutmarianao.proyecto.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
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
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupToolbar()
        setupRegisterText()
        setupLoginButton()
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

    private fun setupRegisterText() {
        val registerText: TextView = findViewById(R.id.register_text)
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLoginButton() {
        val loginButton: View = findViewById(R.id.login_button)
        loginButton.setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.email_edit_text).text.toString().trim()
            val password = findViewById<TextInputEditText>(R.id.password_edit_text).text.toString().trim()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            findViewById<TextInputLayout>(R.id.email_input_layout).error = "El correu és obligatori"
            return false
        }
        if (password.isEmpty()) {
            findViewById<TextInputLayout>(R.id.password_input_layout).error = "La contrasenya és obligatòria"
            return false
        }
        return true
    }

    private fun loginUser(email: String, password: String) {
        val url = "http://10.0.2.2:8080/Usuaris/login"  // La URL del endpoint Spring Boot

        val jsonBody = JSONObject().apply {
            put("correu", email)
            put("contrasenya", password)
        }

        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonBody,
            Response.Listener { response ->
                val cleanedResponse = response.toString().trim()
                Log.d("LoginActivity", "Response: $cleanedResponse")

                try {
                    val jsonResponse = JSONObject(cleanedResponse)

                    // Verifica el mensaje dentro del JSON
                    val status = jsonResponse.optString("status")

                    if (status == "success") {
                        // Si la respuesta contiene "success", busca el ID de usuario
                        fetchUserIdAndSave(email)

                        val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                        prefs.edit()
                            .putBoolean("is_logged_in", true)
                            .apply()

                        val intent = Intent(this, ClientActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val message = jsonResponse.optString("message", "Error desconocido")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "Error al procesar la respuesta: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                val statusCode = error.networkResponse?.statusCode
                val data = error.networkResponse?.data?.toString(Charsets.UTF_8)
                Log.e("VolleyError", "Error $statusCode: $data")
            }

        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }



    private fun fetchUserIdAndSave(email: String) {
        val url = "http://10.0.2.2:8080/Usuaris/get_user_id"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonBody = JSONObject().apply {
            put("correu", email)  // Enviamos el correo en el cuerpo de la solicitud
        }

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, url, jsonBody,
            Response.Listener { response ->
                try {
                    // Asegúrate de que la respuesta es un String
                    val jsonResponse = JSONObject(response.toString())  // Convertimos la respuesta a String si es necesario

                    val status = jsonResponse.getString("status")

                    if (status == "success") {
                        val userId = jsonResponse.getInt("id_usuari")
                        Log.d("LoginActivity", "ID del usuario logueado: $userId")

                        val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                        prefs.edit()
                            .putBoolean("is_logged_in", true)
                            .putInt("id_usuari", userId)
                            .apply()

                        val intent = Intent(this, ClientActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        val message = jsonResponse.getString("message")
                        // Asegurarse de que message es un String antes de mostrarlo en el Toast
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "Error al procesar la respuesta: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error al recuperar l'id de l'usuari: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }




}
