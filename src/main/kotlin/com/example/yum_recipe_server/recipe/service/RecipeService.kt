package com.example.yum_recipe_server.recipe.service

import com.example.yum_recipe_server.recipe.entity.Recipe
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.stereotype.Service

@Service
class RecipeService {
    private val baseUrl : String = "https://www.10000recipe.com/recipe/list.html?q="

    fun getCrawlingRecipes(keyword : String, page : Int) : List<Recipe> {
        val searchUrl : String = "$baseUrl$keyword&order=reco&page=$page"
        val document : Document = Jsoup.connect(searchUrl).get()
        val elements : Elements = document.select("li.common_sp_list_li")
        return elements.map { e ->
            val image = e.select("div.common_sp_thumb img").attr("src")
            val name = e.select("div.common_sp_caption_tit.line2").text()
            val url = e.select("div.common_sp_thumb a").attr("href")
            Recipe(image, name, url)
        }
    }
}