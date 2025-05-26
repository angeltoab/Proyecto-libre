package cat.institutmarianao.proyecto.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import cat.institutmarianao.proyecto.NetworkService
import cat.institutmarianao.proyecto.R
import cat.institutmarianao.proyecto.admin.AdminActivity
import cat.institutmarianao.proyecto.cuiner.MainActivity
import com.google.android.material.navigation.NavigationView
import org.json.JSONArray

class ClientActivity : AppCompatActivity() {

    private lateinit var meals: List<Meal>
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: MealCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val fromMainActivity = intent.getBooleanExtra("from_main_activity", false)

        if (fromMainActivity) {
            // Borrar sesión y número de mesa
            prefs.edit()
                .remove("numero_taula")
                .remove("id_usuari")
                .putBoolean("is_logged_in", false)
                .apply()
        }

        val numeroTaula = prefs.getInt("numero_taula", -1)
        if (numeroTaula == -1) {
            showTableDialog()
        }

        setupToolbar()
        setupDrawer()

        val url = "http://10.0.2.2:8080/plats"
        val networkService = NetworkService(this)

        networkService.fetchMeals(url,
            { response ->
                meals = parseMeals(response)
                setupTabsAndViewPager()
            },
            { error ->
                error.printStackTrace()
            }
        )
    }

    private fun setupToolbar() {
        val myToolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolbar)
        supportActionBar?.title = "Client"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.client_button, menu)

        val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            menu?.findItem(R.id.action_button)?.title = "Tanca Sessió"
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_button -> {
                val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                val isLoggedIn = prefs.getBoolean("is_logged_in", false)

                if (isLoggedIn) {
                    // Guardar numero_taula antes de limpiar
                    val numeroTaula = prefs.getInt("numero_taula", -1)

                    prefs.edit().clear().apply()

                    if (numeroTaula != -1) {
                        prefs.edit()
                            .putInt("numero_taula", numeroTaula)
                            .putInt("id_usuari", 1)
                            .putBoolean("is_logged_in", false)
                            .apply()
                    }

                    Log.d("ClientActivity", "id_usuari después de logout: ${prefs.getInt("id_usuari", 1)}")

                    // Reiniciar actividad
                    val intent = Intent(this, ClientActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                    prefs.edit()
                        .putInt("id_usuari", 1)
                        .putBoolean("is_logged_in", true)
                        .apply()
                }

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        val myToolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        myToolbar.setNavigationIcon(R.drawable.ic_menu)
        myToolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.nav_client -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_admin -> {
                    startActivity(Intent(this, AdminActivity::class.java))
                }
            }
            true
        }
    }

    private fun showTableDialog() {
        val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        val numeroTaula = prefs.getInt("numero_taula", -1)
        if (numeroTaula != -1) return  // Ya hay mesa, no mostrar diálogo
        val fromMainActivity = intent.getBooleanExtra("from_main_activity", false)

        if (!fromMainActivity) return // Solo mostrar si vienes de MainActivity

        val editText = android.widget.EditText(this).apply {
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            hint = "Número de taula"
        }

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("A quina taula s'ubicará aquesta tablet?")
            .setView(editText)
            .setCancelable(false)
            .create()

        dialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Aceptar") { _, _ -> }

        dialog.setOnShowListener {
            val button = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val numeroTaula = editText.text.toString()
                if (numeroTaula.isNotEmpty()) {
                    checkTableExists(numeroTaula.toInt()) { exists ->
                        if (exists) {
                            prefs.edit().putInt("numero_taula", numeroTaula.toInt()).apply()
                            dialog.dismiss()
                        } else {
                            editText.error = "Aquesta taula no existeix"
                        }
                    }
                } else {
                    editText.error = "Introdueix un número"
                }
            }
        }

        dialog.show()
    }

    private fun checkTableExists(tableNumber: Int, callback: (Boolean) -> Unit) {
        val url = "http://10.0.2.2:8080/Taulas/existeix?id_taula=$tableNumber"

        val request = com.android.volley.toolbox.StringRequest(
            com.android.volley.Request.Method.GET, url,
            { response ->
                // Se espera una respuesta "true" o "false" desde el servicio Spring Boot
                callback(response.trim() == "true")
            },
            { error ->
                error.printStackTrace()
                callback(false)
            }
        )

        val queue = com.android.volley.toolbox.Volley.newRequestQueue(this)
        queue.add(request)
    }


    private fun parseMeals(response: JSONArray): List<Meal> {
        val meals = mutableListOf<Meal>()
        for (i in 0 until response.length()) {
            val obj = response.getJSONObject(i)
            val meal = Meal(
                id_plat = obj.getInt("id_plat"),
                name = obj.getString("nom"),
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

    private fun setupTabsAndViewPager() {
        val prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        adapter = MealCategoryAdapter(this, meals, isLoggedIn)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Recomanacions"
                1 -> "Entrants"
                2 -> "Principals"
                3 -> "Postres"
                4 -> "Begudes"
                5 -> if (isLoggedIn) "Historial" else ""
                else -> ""
            }
        }.attach()
    }
}
