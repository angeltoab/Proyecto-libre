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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class TableManagementActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: TableAdapter
    private val tables = mutableListOf<Table>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_management)
        setupToolbar()

        listView = findViewById(R.id.tablesListView)
        setupAdapter()
        loadTables()

        val createButton: Button = findViewById(R.id.createButton)
        createButton.setOnClickListener {
            val intent = Intent(this, CreateTableActivity::class.java)
            startActivityForResult(intent, 1001)
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val table = tables[position]
            showOptionsDialog(table)
            true
        }
    }

    private fun setupAdapter() {
        adapter = TableAdapter(this, tables)
        listView.adapter = adapter
    }

    private fun loadTables() {
        val url = "http://10.0.2.2:8080/Taulas"

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response: JSONArray ->
                tables.clear()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    val table = Table(
                        id = item.getInt("id"),
                        capacity = item.getInt("capacitat")
                    )
                    tables.add(table)
                }
                tables.sortByDescending { it.id }
                adapter.notifyDataSetChanged()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al cargar mesas", Toast.LENGTH_SHORT).show()
            })

        Volley.newRequestQueue(this).add(request)
    }

    private fun showOptionsDialog(table: Table) {
        val options = arrayOf("Editar", "Eliminar")

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Opciones para Mesa ${table.id}")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> editTable(table)
                1 -> showDeleteConfirmationDialog(table)
            }
        }
        builder.show()
    }

    private fun editTable(table: Table) {
        val intent = Intent(this, EditTableActivity::class.java).apply {
            putExtra("table", table)
        }
        startActivityForResult(intent, 1002)
    }

    private fun showDeleteConfirmationDialog(table: Table) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Eliminar mesa")
        builder.setMessage("¿Estás seguro que quieres eliminar la mesa ${table.id}?")
        builder.setPositiveButton("Sí") { _, _ ->
            deleteTable(table)
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    private fun deleteTable(table: Table) {
        val checkUrl = "http://10.0.2.2:8080/Taulas/${table.id}/hasOrders"

        val checkRequest = JsonObjectRequest(
            Request.Method.GET, checkUrl, null,
            { response ->
                val hasPedidos = response.getBoolean("hasOrders")
                if (hasPedidos) {
                    Toast.makeText(
                        this,
                        "No se puede eliminar la mesa ${table.id} porque tiene pedidos asociados",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    proceedWithDelete(table)
                }
            },
            { error ->
                Toast.makeText(this, "Error al verificar pedidos", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            })

        Volley.newRequestQueue(this).add(checkRequest)
    }

    private fun proceedWithDelete(table: Table) {
        val deleteUrl = "http://10.0.2.2:8080/Taulas/${table.id}"

        val deleteRequest = com.android.volley.toolbox.StringRequest(
            Request.Method.DELETE, deleteUrl,
            {
                Toast.makeText(this, "Mesa eliminada correctamente", Toast.LENGTH_SHORT).show()
                loadTables()
            },
            { error ->
                Toast.makeText(this, "Error al eliminar la mesa", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            })

        Volley.newRequestQueue(this).add(deleteRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1001 || requestCode == 1002) && resultCode == Activity.RESULT_OK) {
            loadTables()
        }
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
}

data class Table(
    val id: Int,
    val capacity: Int
) : java.io.Serializable