package com.app.restapiapp.models

data class Users(
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int,
    val data: List<User>
)