package cat.institutmarianao.proyecto.cuiner

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cat.institutmarianao.proyecto.R
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


data class Order(
    val imageResId: Int,
    val name: String,
    val tableId: String,
    val idComanda: Int
)

class OrderAdapter(context: Context, orders: MutableList<Order>) :
    ArrayAdapter<Order>(context, R.layout.list_item_cuiner, orders) {

    private class ViewHolder(view: View) {
        val image: ImageView = view.findViewById(R.id.mealImage)
        val name: TextView = view.findViewById(R.id.mealName)
        val table: TextView = view.findViewById(R.id.tableID)
        val button: Button = view.findViewById(R.id.button)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val meal = getItem(position)
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_cuiner, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        holder.apply {
            image.setImageResource(meal?.imageResId ?: R.drawable.ic_launcher_foreground)
            name.text = meal?.name
            table.text = meal?.tableId
            button.setOnClickListener {
                meal?.let { sendOrder(it) }
            }
        }

        return view
    }

    private fun sendOrder(order: Order) {
        val url = "http://10.0.2.2:8080/comandes/update"
        val request = object : StringRequest(Method.POST, url,
            { response ->
                remove(order)
                notifyDataSetChanged()
            },
            { error ->
                Log.e("OrderAdapter", "Error al actualizar comanda", error)
                Toast.makeText(context, "Error al enviar comanda", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf("id_comanda" to order.idComanda.toString())
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}

