package com.example.newsandroid.ui.detail

import android.app.Application
import com.example.newsandroid.domain.NewsProperty
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.mockk


class DetailNewsViewModelTest {

    @MockK
    private lateinit var application: Application

    @InjectMockKs
    private lateinit var viewModel: DetailNewsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = DetailNewsViewModel(application)
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()



    val title = "Le métro parisien enfin couvert en 4G à 100%"
    val author = "Jean-Yves Alric"
    val description = "La RATP a réalisé ce déploiement en partenariat avec Bouygues Telecom, Free Mobile, SFR et Orange."
    val url = "https://www.presse-citron.net/le-metro-parisien-enfin-couvert-en-4g-a-100/"
    val urlToImage = "https://www.presse-citron.net/wordpress_prod/wp-content/uploads/2019/09/metro-passe-navigo-tickets-paris.jpg"
    val publishedAt = "2020-06-30T06:00:23Z"
    val content = "Cest lachèvement de longs travaux qui ont mobilisé les plus grands opérateurs téléphoniques français. La RATP vient dannoncer ce lundi que lensemble de son réseau est couvert à 100 % en 4G. Cest une … [+1682 chars]"

    @Test
    fun `getNewsDetail() with empty news`() {
        val news = NewsProperty()

        viewModel.getNewsDetail(news)

        assertEquals(viewModel.news.value?.title, "")
        assertEquals(viewModel.news.value?.author, "")
        assertEquals(viewModel.news.value?.description, "")
        assertEquals(viewModel.news.value?.url, "")
        assertEquals(viewModel.news.value?.urlToImage, "")
        assertEquals(viewModel.news.value?.publishedAt, "")
        assertEquals(viewModel.news.value?.content, "")
        assertEquals(viewModel.uriImageString, "")
        assertEquals(viewModel.uri, "")
    }

    @Test
    fun `getNewsDetail() with news`() {
        val news = NewsProperty(title, author, description, url, urlToImage, publishedAt, content)

        viewModel.getNewsDetail(news)

        assertEquals(viewModel.news.value?.title, title)
        assertEquals(viewModel.news.value?.author, author)
        assertEquals(viewModel.news.value?.description, description)
        assertEquals(viewModel.news.value?.url, url)
        assertEquals(viewModel.news.value?.urlToImage, urlToImage)
        assertEquals(viewModel.news.value?.publishedAt, publishedAt)
        assertEquals(viewModel.news.value?.content, content)
        assertEquals(viewModel.uriImageString, urlToImage)
        assertEquals(viewModel.uri, url)
    }

}
