package com.example.yum_recipe_server.recipe.controller

import com.example.yum_recipe_server.recipe.entity.Recipe
import com.example.yum_recipe_server.recipe.service.RecipeService
import io.swagger.v3.oas.annotations.Operation
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
}