package cat.institutmarianao.proyecto.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import cat.institutmarianao.proyecto.R
import cat.institutmarianao.proyecto.client.ClientActivity
import cat.institutmarianao.proyecto.cuiner.MainActivity
import com.google.android.material.navigation.NavigationView

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        setupToolbar()
        setupDrawer()
        val mealButton: Button = findViewById(R.id.btn_plats)
        val tableButton: Button = findViewById(R.id.btn_taules)

        mealButton.setOnClickListener{
             intent = Intent(this, MealsManagementActivity::class.java)
            startActivity(intent)
        }
        tableButton.setOnClickListener{
             intent = Intent(this, TableManagementActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setupToolbar() {
        val myToolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(myToolbar)
        supportActionBar?.title = "Administrador"
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
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
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