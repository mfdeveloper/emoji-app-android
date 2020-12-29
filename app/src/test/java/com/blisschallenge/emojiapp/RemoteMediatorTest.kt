package com.blisschallenge.emojiapp

import com.blisschallenge.emojiapp.models.repositories.mediators.GithubReposMediator
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class RemoteMediatorTest {

    private lateinit var mediator: GithubReposMediator

    private val headerValid = "<https://api.github.com/user/xxxxx/repos?page=14>; rel=\"prev\", " +
            "<https://api.github.com/user/xxxxx/repos?page=16>; rel=\"next\", " +
            "<https://api.github.com/user/xxxxx/repos?page=64>; rel=\"last\", <https://api.github.com/user/xxxxx/repos?page=1>; rel=\"first\""

    private val headerWithNext = "<https://api.github.com/user/xxxxx/repos?per_page=50&page=2>; rel=\"next\", " +
            "<https://api.github.com/user/xxxxx/repos?page=64>; rel=\"last\", <https://api.github.com/user/xxxxx/repos?page=1>; rel=\"first\""

    private val headerWithoutNext = "<https://api.github.com/user/xxxxx/repos?page=4&per_page=20>; rel=\"prev\", " +
            "<https://api.github.com/user/xxxxx/repos?page=1&per_page=20>; rel=\"first\""

    @Before
    fun setUp() {

        // TODO: See how mock [RoomDatabase] instance
        mediator = GithubReposMediator("")
    }

    @Test
    fun checkHeaderNextValue() {

        assertThat(mediator.getHeaderKeys(headerValid)).containsExactlyEntriesIn(mapOf("prevKey" to 14, "nextKey" to 16))
        assertThat(mediator.getHeaderKeys(headerWithNext)).containsExactlyEntriesIn(mapOf("nextKey" to 2))
    }

    @Test
    fun checkHeaderPrevValue() {

        val pagingKeys = mediator.getHeaderKeys(headerWithoutNext)
        assertThat(pagingKeys).containsEntry("prevKey", 4)
        assertThat(pagingKeys).doesNotContainKey("nextKey")
    }
}