package com.example.yum_recipe_server.recipe.service

import com.example.yum_recipe_server.recipe.entity.Recipe
import com.example.yum_recipe_server.recipe.repository.RecipeRepository
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable

@Service
class RecipeService(val recipeRepository: RecipeRepository) {
    private val baseUrl: String = "https://www.10000recipe.com/recipe/list.html?q="

    /**
     * 레시피 크롤링 기능
     */
    fun getCrawlingRecipes(keyword: String, page: Int): List<Recipe> {
        return try {
            val searchUrl = "$baseUrl$keyword&order=reco&page=$page"
            val document = Jsoup.connect(searchUrl).get()
            val elements = document.select("li.common_sp_list_li")
            val recipes = elements.mapNotNull { e ->
                val image = e.select("div.common_sp_thumb img").attr("src")
                val name = e.select("div.common_sp_caption_tit.line2").text()
                val url = e.select("div.common_sp_thumb a").attr("href")
                if (name.isNotEmpty() && url.isNotEmpty()) {
                    Recipe(imageUrl = image, name = name, url = url)
                } else {
                    null
                }
            }
            // 중복 제거 후 저장
            val uniqueRecipes = recipes.distinctBy { it.url }
            recipeRepository.saveAll(uniqueRecipes)
            uniqueRecipes
        } catch (e: Exception) {
            println("크롤링 실패: ${e.message}")
            emptyList()
        }
    }

    /**
     * 단일 키워드 기반 레시피 검색
     */
    fun searchRecipes(keyword: String, pageable: Pageable): List<Recipe> {
        return try {
            if (keyword.isBlank()) {
                println("키워드가 비어 있습니다.")
                emptyList()
            } else {
                recipeRepository.findByNameContaining(keyword, pageable)
            }
        } catch (e: Exception) {
            println("검색 실패: ${e.message}")
            emptyList()
        }
    }

    /**
     * 다중 키워드 기반 레시피 검색
     */
    fun searchRecipesByKeywords(keywords: List<String>, pageable: Pageable): List<Recipe> {
        return try {
            if (keywords.isEmpty()) {
                println("키워드 리스트가 비어 있습니다.")
                emptyList()
            } else {
                val results = keywords.flatMap { keyword ->
                    recipeRepository.findByNameContaining(keyword, pageable)
                }
                results.distinctBy { it.id } // 중복 제거
            }
        } catch (e: Exception) {
            println("다중 키워드 검색 실패: ${e.message}")
            emptyList()
        }
    }
}