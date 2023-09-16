package com.example.itami_chat.core.domain.paginator

interface Paginator {

    suspend fun loadNextPage()

    fun reset()
}