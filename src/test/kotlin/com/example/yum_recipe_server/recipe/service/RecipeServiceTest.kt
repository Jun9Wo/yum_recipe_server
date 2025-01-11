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
    private lateinit var recipeService: RecipeService

    @BeforeEach
    fun setup() {
        recipeRepository = mock(RecipeRepository::class.java) // Mock 객체 생성
        recipeService = RecipeService(recipeRepository)       // Mock Repository 주입
    }

    @Test
    fun `크롤링 정보 수행 테스트`() {
        val mockRecipes = listOf(
            Recipe(id = "1", imageUrl = "http://example.com/image.jpg", name = "테스트 레시피", url = "http://example.com/recipe")
        )
        Mockito.`when`(recipeRepository.saveAll(mockRecipes)).thenReturn(mockRecipes)

        val result = recipeService.getCrawlingRecipes("테스트", 1)
        assert(result.isNotEmpty()) { "크롤링 결과가 비어 있습니다." }
        assert(result.size == 1) { "크롤링된 레시피 개수가 예상과 다릅니다." }
        assert(result[0].name == "테스트 레시피") { "크롤링된 레시피 이름이 일치하지 않습니다." }
        println(result)
    }

    @Test
    fun `단일 키워드 검색 테스트`() {
        val pageable = PageRequest.of(0, 10) // 테스트용 Pageable
        val mockResults = listOf(
            Recipe(id = "1", imageUrl = "http://example.com/image.jpg", name = "김치찌개", url = "http://example.com/recipe")
        )
        Mockito.`when`(recipeRepository.findByNameContaining("김치", pageable)).thenReturn(mockResults)

        val results = recipeService.searchRecipes("김치", pageable)
        assert(results.isNotEmpty()) { "검색 결과가 비어 있습니다." }
        assert(results.size == 1) { "검색된 레시피 개수가 예상과 다릅니다." }
        assert(results[0].name.contains("김치")) { "검색된 레시피 이름이 키워드를 포함하지 않습니다." }
        println(results)
    }

    @Test
    fun `다중 키워드 검색 테스트`() {
        val pageable = PageRequest.of(0, 10) // 테스트용 Pageable
        val mockResultsForKeyword1 = listOf(
            Recipe(id = "1", imageUrl = "http://example.com/image1.jpg", name = "김치찌개", url = "http://example.com/recipe1")
        )
        val mockResultsForKeyword2 = listOf(
            Recipe(id = "2", imageUrl = "http://example.com/image2.jpg", name = "된장찌개", url = "http://example.com/recipe2")
        )

        Mockito.`when`(recipeRepository.findByNameContaining("김치", pageable)).thenReturn(mockResultsForKeyword1)
        Mockito.`when`(recipeRepository.findByNameContaining("된장", pageable)).thenReturn(mockResultsForKeyword2)

        val keywords = listOf("김치", "된장")
        val results = recipeService.searchRecipesByKeywords(keywords, pageable)

        assert(results.isNotEmpty()) { "다중 키워드 검색 결과가 비어 있습니다." }
        assert(results.size == 2) { "다중 키워드 검색된 레시피 개수가 예상과 다릅니다." }
        assert(results.any { it.name.contains("김치") }) { "김치 키워드 결과가 포함되지 않았습니다." }
        assert(results.any { it.name.contains("된장") }) { "된장 키워드 결과가 포함되지 않았습니다." }
        println(results)
    }

    @Test
    fun `검색 키워드가 비어있을 때 테스트`() {
        val pageable = PageRequest.of(0, 10)
        val results = recipeService.searchRecipes("", pageable)
        assert(results.isEmpty()) { "빈 키워드로 검색했을 때 결과가 존재합니다." }
    }
}