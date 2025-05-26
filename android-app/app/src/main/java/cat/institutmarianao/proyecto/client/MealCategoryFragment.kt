package cat.institutmarianao.proyecto.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.core.content.ContentProviderCompat.requireContext
import cat.institutmarianao.proyecto.databinding.FragmentMealCategoryBinding

class MealCategoryFragment : Fragment() {

    private lateinit var category: String
    private lateinit var meals: List<Meal>

    // Mapea el nombre de la pestaña a la categoría real en la base de datos
    private fun mapCategory(tabName: String): String {
        return when (tabName) {
            "Entrants" -> "Entrant"
            "Principals" -> "Principal"
            "Postres" -> "Postre"
            "Begudes" -> "Beguda"
            else -> tabName // Para "Recomanacions" no cambia
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMealCategoryBinding.inflate(inflater, container, false)

        // Filtrar los platos según la categoría
        val filteredMeals = if (category == "Recomanacions") {
            meals.filter { it.recommendation.equals("Si", ignoreCase = true) }
        } else {
            val realCategory = mapCategory(category)
            meals.filter { it.category.equals(realCategory, ignoreCase = true) }
        }

        // Configurar el adaptador
        val adapter = MealAdapter(requireContext(), filteredMeals)
        binding.mealsListView.adapter = adapter

        return binding.root
    }

    // Setter para la categoría
    fun setCategory(category: String) {
        this.category = category
    }

    // Setter para los platos
    fun setMeals(meals: List<Meal>) {
        this.meals = meals
    }
}
