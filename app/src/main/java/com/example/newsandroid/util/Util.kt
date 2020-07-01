package com.example.newsandroid.util

import com.example.newsandroid.enums.Category
import com.example.newsandroid.enums.Country

fun createChipList(): List<String>{
    val list : List<String> = listOf(
        Country.FR.toString(),
        Country.US.toString())
    return list
}

fun createChipCategoryList(): List<String>{
    val list : List<String> = listOf(
        Category.BUSINESS.value,
        Category.ENTERTAINMENT.value,
        Category.HEALTH.value,
        Category.SCIENCE.value,
        Category.SPORTS.value,
        Category.TECHNOLOGY.value
    )
    return list
}

fun transformSpinnerStringToParametersApi(parameter: String):String{
    when(parameter){
        "Dernières News" -> {
            return "publishedAt"
        }
        "Populaire" -> {
            return "popularity"
        }
        "Pertinence" -> {
            return "relevancy"
        }
    }
    return "publishedAt"
}