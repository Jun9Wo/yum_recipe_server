package com.example.yum_recipe_server.recipe.service

import com.example.yum_recipe_server.recipe.entity.Recipe
import com.example.yum_recipe_server.recipe.repository.RecipeRepository
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable

@Service
class RecipeService(val recipeRepository: RecipeRepository) {
    private val baseUrl: String = "https://www.10000recipe.com/recipe/list.html?q="

    fun getCrawlingRecipes(keyword: String, page: Int): List<Recipe> {
        return try {
            val searchUrl = "$baseUrl$keyword&order=reco&page=$page"
            val document = Jsoup.connect(searchUrl).get()
            val elements = document.select("li.common_sp_list_li")
            val recipes = elements.map { e ->
                val image = e.select("div.common_sp_thumb img").attr("src")
                val name = e.select("div.common_sp_caption_tit.line2").text()
                val url = e.select("div.common_sp_thumb a").attr("href")
                Recipe(imageUrl = image, name = name, url = url)
            }
            recipeRepository.saveAll(recipes)
            recipes
        } catch (e: Exception) {
            println("크롤링 실패: ${e.message}")
            emptyList()
        }
    }

    fun searchRecipes(keyword: String, pageable: Pageable): List<Recipe> {
        return recipeRepository.findByNameContaining(keyword, pageable)
    }
}
