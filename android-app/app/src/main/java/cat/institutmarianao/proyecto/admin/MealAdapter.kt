package cat.institutmarianao.proyecto.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import cat.institutmarianao.proyecto.R

class MealAdapter(
    context: Context,
    private val plats: List<Meal>,
    private val onRecommendationChanged: (Meal, Boolean) -> Unit
) : ArrayAdapter<Meal>(context, 0, plats) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_meals_management, parent, false)

        val meal = plats[position]

        view.findViewById<TextView>(R.id.mealName).text = meal.name
        view.findViewById<TextView>(R.id.mealDescription).text = meal.description
        view.findViewById<TextView>(R.id.mealCategory).text = meal.category
        view.findViewById<TextView>(R.id.mealPrice).text = "%.2f€".format(meal.price)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        checkBox.isChecked = meal.recommendation == "Si"

        // Configurar el listener para cambios en el CheckBox
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            onRecommendationChanged(meal, isChecked)
        }

        return view
    }
}