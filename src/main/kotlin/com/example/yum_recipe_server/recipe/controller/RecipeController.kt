package com.example.yum_recipe_server.recipe.controller

import com.example.yum_recipe_server.recipe.entity.Recipe
import com.example.yum_recipe_server.recipe.service.RecipeService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recipes")
class RecipeController(val recipeService: RecipeService) {

    /**
     * 천개의 레시피 크롤링 API
     */
    @Operation(
        summary = "천개의 레시피 크롤링",
        description = "검색 키워드 및 페이지를 입력받아 레시피를 크롤링하여 반환"
    )
    @GetMapping
    fun getRecipes(@RequestParam keyword: String, @RequestParam page: Int): List<Recipe> {
        if (keyword.isBlank()) {
            println("키워드가 비어 있습니다.")
            return emptyList()
        }
        return recipeService.getCrawlingRecipes(keyword, page)
    }

    /**
     * 단일 키워드 검색 API
     */
    @Operation(
        summary = "레시피 단일 키워드 검색",
        description = "단일 키워드를 입력받아 엘라스틱서치에서 레시피 검색"
    )
    @GetMapping("/search")
    fun searchRecipes(@RequestParam keyword: String, @RequestParam page: Int): List<Recipe> {
        if (keyword.isBlank()) {
            println("키워드가 비어 있습니다.")
            return emptyList()
        }
        val pageable = PageRequest.of(page, 10) // 페이지당 10개
        return recipeService.searchRecipes(keyword, pageable)
    }

    /**
     * 다중 키워드 검색 API
     */
    @Operation(
        summary = "레시피 다중 키워드 검색",
        description = "여러 키워드를 입력받아 엘라스틱서치에서 레시피를 검색"
    )
    @GetMapping("/search/multiple")
    fun searchRecipesByKeywords(
        @RequestParam keywords: String,
        @RequestParam page: Int
    ): List<Recipe> {
        val keywordList = keywords.split(",").map { it.trim() }.filter { it.isNotBlank() }
        if (keywordList.isEmpty()) {
            println("키워드 리스트가 비어 있습니다.")
            return emptyList()
        }
        val pageable = PageRequest.of(page, 10)
        return recipeService.searchRecipesByKeywords(keywordList, pageable)
    }
}