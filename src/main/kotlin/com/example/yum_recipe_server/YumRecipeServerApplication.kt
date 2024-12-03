package com.example.yum_recipe_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class YumRecipeServerApplication

fun main(args: Array<String>) {
	runApplication<YumRecipeServerApplication>(*args)
}
