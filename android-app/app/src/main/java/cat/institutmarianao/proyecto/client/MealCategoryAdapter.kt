package cat.institutmarianao.proyecto.client

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class MealCategoryAdapter(
    activity: AppCompatActivity,
    private val meals: List<Meal>,
    private val isLoggedIn: Boolean
) : FragmentStateAdapter(activity) {

    private val categories =
        mutableListOf("Recomanacions", "Entrants", "Principals", "Postres", "Begudes").apply {
            if (isLoggedIn) add("Historial")
        }

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        return if (categories[position] == "Historial") {
            UserOrdersFragment()  // fragmento vacío por ahora
        } else {
            MealCategoryFragment().apply {
                setMeals(meals)
                setCategory(categories[position])
            }
        }
    }
}
