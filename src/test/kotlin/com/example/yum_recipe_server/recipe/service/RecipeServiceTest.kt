package com.example.yum_recipe_server.recipe.service

import com.example.yum_recipe_server.recipe.entity.Recipe
import com.example.yum_recipe_server.recipe.repository.RecipeRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.data.domain.PageRequest

class RecipeServiceTest {
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var recipeService : RecipeService

    @BeforeEach
    fun setup() {
        recipeRepository = mock(RecipeRepository::class.java) // Mock 객체 생성
        recipeService = RecipeService(recipeRepository)      // Mock Repository 주입
    }

    @Test
    fun `크롤링 정보 수행 테스트`() {
        val mockRecipes = listOf(
            Recipe(id = "1", imageUrl = "http://example.com/image.jpg", name = "테스트 레시피", url = "http://example.com/recipe")
        )
        Mockito.`when`(recipeRepository.saveAll(mockRecipes)).thenReturn(mockRecipes)

        val result = recipeService.getCrawlingRecipes("테스트", 1)
        assert(result.isNotEmpty())
        println(result)
    }

    @Test
    fun `검색 테스트`() {
        val pageable = PageRequest.of(0, 10) // 테스트용 Pageable
        val mockResults = listOf(
            Recipe(id = "1", imageUrl = "http://example.com/image.jpg", name = "김치찌개", url = "http://example.com/recipe")
        )
        Mockito.`when`(recipeRepository.findByNameContaining("김치", pageable)).thenReturn(mockResults)

        val results = recipeService.searchRecipes("김치", pageable)
        assert(results.isNotEmpty())
        println(results)
    }
}