package com.example.yum_recipe_server.recipe.repository

import com.example.yum_recipe_server.recipe.entity.Recipe
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.data.domain.Pageable

interface RecipeRepository : ElasticsearchRepository<Recipe, String> {
    fun findByNameContaining(keyword: String, pageable: Pageable): List<Recipe>
}