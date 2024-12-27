package com.example.yum_recipe_server.recipe.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "recipes")
data class Recipe(
    @Id
    var id: String = "",
    val imageUrl: String,
    val name: String,
    val url: String
) {
    init {
        id = url.hashCode().toString()
    }
}

