package cat.institutmarianao.proyecto.cuiner

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import cat.institutmarianao.proyecto.NetworkService
import cat.institutmarianao.proyecto.R
import cat.institutmarianao.proyecto.admin.AdminActivity
import cat.institutmarianao.proyecto.client.ClientActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var meals: MutableList<Order>
    private lateinit var adapter: OrderAdapter
    private val updateInterval = 5000L
    private val url = "http://10.0.2.2:8080/comandes/actives"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupDrawer()
        setupListView()

        setupAutoRefresh()
    }

    private fun setupListView() {
        meals = mutableListOf()
        adapter = OrderAdapter(this, meals)
        findViewById<ListView>(R.id.mealsListView).adapter = adapter
    }

    private fun setupAutoRefresh() {
        handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                fetchOrders()
                handler.postDelayed(this, updateInterval)
            }
        })
    }

    private fun fetchOrders() {
        NetworkService(this).fetchMeals(url,
            { response ->
                meals.clear()
                for (i in 0 until response.length()) {
                    response.getJSONObject(i).let { obj ->
                        val plat = obj.getJSONObject("plat").getString("nom")
                        val taula = "Taula ${obj.getJSONObject("taula").getInt("id")}"
                        val idComanda = obj.getInt("id")
                        meals.add(Order(R.drawable.ic_launcher_foreground, plat, taula, idComanda))
                    }
                }
                adapter.notifyDataSetChanged()
            },
            { error ->
                Log.e("ERROR", "Error al obtener comandas", error)
                Toast.makeText(this, "Error al obtener comandas", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setupToolbar() {
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.my_toolbar).apply {
            setSupportActionBar(this)
            supportActionBar?.title = "Cuiner"
        }
    }

    private fun setupDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)

        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_client -> {
                    startActivity(Intent(this, ClientActivity::class.java).apply {
                        putExtra("from_main_activity", true)
                    })
                }
                R.id.nav_admin -> {
                    startActivity(Intent(this, AdminActivity::class.java))
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }
}
