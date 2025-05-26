package cat.institutmarianao.proyecto.admin

import java.io.Serializable

data class Meal(
    val id_plat: Int,
    val name: String,
    val description: String,
    val category: String,
    var recommendation: String?,
    val price: Double,
    val score: Double // Cambiar a Double para manejar decimales
): Serializable