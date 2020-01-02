package renesme.pierre.td2

import com.squareup.moshi.Json

//data class Task(val id: String, val title: String , val description: String = "Description content") {

data class Task(
    @field:Json(name = "id")
    val id: String,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "description")
    val description: String = "Description content"
)