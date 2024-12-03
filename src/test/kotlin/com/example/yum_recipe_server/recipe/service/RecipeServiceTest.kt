package com.example.yum_recipe_server.recipe.service

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RecipeServiceTest {
    private lateinit var recipeService : RecipeService

    @BeforeEach
    fun setup() {
        recipeService = RecipeService()
    }

    @Test
    fun `크롤링 정보 수행 테스트`() {
        val result = recipeService.getCrawlingRecipes("김치찌개", 1)
        println(result)
        assert(result.isNotEmpty())
    }
}