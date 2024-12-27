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
        return recipeService.getCrawlingRecipes(keyword, page)
    }

    /**
     * 검색 API
     */
    @Operation(
        summary = "레시피 검색",
        description = "키워드를 입력받아 엘라스틱서치에서 레시피 검색"
    )
    @GetMapping("/search")
    fun searchRecipes(@RequestParam keyword: String, @RequestParam page: Int): List<Recipe> {
        val pageable = PageRequest.of(page, 10) // 페이지당 10개
        return recipeService.searchRecipes(keyword, pageable)
    }
}