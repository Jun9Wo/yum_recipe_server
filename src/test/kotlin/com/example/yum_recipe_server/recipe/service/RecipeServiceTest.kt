package com.example.yum_recipe_server.recipe.service

import com.example.yum_recipe_server.recipe.entity.Recipe
import com.example.yum_recipe_server.recipe.repository.RecipeRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

class RecipeServiceTest {

    private lateinit var recipeRepository: RecipeRepository
    private lateinit var recipeService: RecipeService

    @BeforeEach
    fun setup() {
        // Mock 객체 생성
        recipeRepository = mock(RecipeRepository::class.java)
        // Mock Repository 주입
        recipeService = RecipeService(recipeRepository)
    }

    @Test
    @DisplayName("크롤링 정보 수행 테스트")
    fun `크롤링 정보 수행 테스트`() {
        // given
        val mockRecipes = listOf(
            Recipe(
                id = "1",
                imageUrl = "http://example.com/image.jpg",
                name = "테스트 레시피",
                url = "http://example.com/recipe"
            )
        )
        Mockito.`when`(recipeRepository.saveAll(anyList())).thenReturn(mockRecipes)

        val result = recipeService.getCrawlingRecipes("테스트", 1)

        assertTrue(result.isNotEmpty(), "크롤링 결과는 비어있으면 안 됩니다.")
        println("크롤링 결과: $result")
    }

    @Test
    @DisplayName("단일 키워드 검색 테스트")
    fun `검색 테스트`() {
        // given
        val pageable: Pageable = PageRequest.of(0, 10)
        val mockResults = listOf(
            Recipe(
                id = "1",
                imageUrl = "http://example.com/image.jpg",
                name = "김치찌개",
                url = "http://example.com/recipe"
            )
        )
        Mockito.`when`(recipeRepository.findByNameContaining("김치", pageable)).thenReturn(mockResults)

        val results = recipeService.searchRecipes("김치", pageable)

        assertTrue(results.isNotEmpty(), "검색 결과는 비어있으면 안 됩니다.")
        assertEquals("김치찌개", results.first().name)
        println("검색 결과: $results")
    }

    @Test
    @DisplayName("빈 키워드 검색 테스트")
    fun `빈 키워드 검색 테스트`() {
        val pageable: Pageable = PageRequest.of(0, 10)

        val results = recipeService.searchRecipes("", pageable)

        assertTrue(results.isEmpty(), "빈 키워드를 검색하면 결과도 비어있어야 합니다.")
        println("빈 키워드 검색 결과: $results")
    }

    @Test
    @DisplayName("다중 키워드 검색 테스트")
    fun `다중 키워드 검색 테스트`() {
        val pageable: Pageable = PageRequest.of(0, 10)
        val mockResults1 = listOf(
            Recipe(
                id = "1",
                imageUrl = "http://example.com/image.jpg",
                name = "김치찌개",
                url = "http://example.com/recipe"
            )
        )
        val mockResults2 = listOf(
            Recipe(
                id = "2",
                imageUrl = "http://example.com/image2.jpg",
                name = "된장찌개",
                url = "http://example.com/recipe2"
            )
        )

        // 특정 키워드별로 다른 리스트를 리턴하도록 Mock 설정
        Mockito.`when`(recipeRepository.findByNameContaining("김치", pageable)).thenReturn(mockResults1)
        Mockito.`when`(recipeRepository.findByNameContaining("된장", pageable)).thenReturn(mockResults2)

        val results = recipeService.searchRecipesByKeywords(listOf("김치", "된장"), pageable)

        assertEquals(2, results.size, "두 가지 키워드 검색 시 총 2개의 결과가 있어야 합니다.")
        println("다중 키워드 검색 결과: $results")
    }

    @Test
    @DisplayName("빈 키워드 리스트 검색 테스트")
    fun `빈 키워드 리스트 검색 테스트`() {
        val pageable: Pageable = PageRequest.of(0, 10)

        val results = recipeService.searchRecipesByKeywords(emptyList(), pageable)

        assertTrue(results.isEmpty(), "빈 키워드 리스트로 검색 시 결과도 비어있어야 합니다.")
        println("빈 키워드 리스트 검색 결과: $results")
    }
}