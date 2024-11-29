package com.example.api_consuming.model

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)