package cat.institutmarianao.proyecto.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cat.institutmarianao.proyecto.R

class TableAdapter(context: Context, private val tables: List<Table>) :
    ArrayAdapter<Table>(context, 0, tables) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_table_management, parent, false)

        val table = tables[position]

        view.findViewById<TextView>(R.id.tableId).text = "Mesa #${table.id}"
        view.findViewById<TextView>(R.id.tableCapacity).text = "${table.capacity} personas"

        return view
    }
}