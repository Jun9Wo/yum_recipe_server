package com.example.yum_recipe_server

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class YumRecipeServerApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Test
	fun `Elasticsearch Health Check`() {
		val response = true // Elasticsearch 클라이언트 핑 상태 체크 로직 필요
		assert(response)
	}
}
