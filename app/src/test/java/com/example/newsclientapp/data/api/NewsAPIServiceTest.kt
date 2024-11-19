package com.example.newsclientapp.data.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsAPIServiceTest {
    private lateinit var service:NewsAPIService
    private lateinit var server:MockWebServer

    @Before
    fun setUp() { //method annotated with before runs before thr test
        server=MockWebServer()
        service=Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPIService::class.java)
    }

  /*We created this JSON response file named newsresponse. But, MockWebServer can’t read this response
from the JSON file directly. We need to create a file reader to read the contents of the JSON
file and convert them into a String object.
After that we need to add that String MockResponse to the queue of the MockWebServer,
The first request to the MockWebServer will be replied with the first enqueued response,
second with the second response.
That’s how MockWebServer works.*/



    private fun enqueueMockResponse(
        fileName:String
    ){
        val inputStream=javaClass.classLoader!!.getResourceAsStream(fileName)
        val source=inputStream.source().buffer()
        val mockResponse=MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }

    @Test
    fun getTopHeadLines_sentRequest_recievedExpected(){
        runBlocking {
            enqueueMockResponse("newsResponse.json")
            val responseBody=service.getTopHeadlines("us",1).body() //this will go with url endpoint and all to the mockserver
            val request=server.takeRequest()  //server will get tht address
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/v2/top-headlines?country=us&page=1&apiKey=aef3624543264380b1e7570c2a7f122a")
        }
    }


    @Test
    fun getTopHeadlines_recievedResponse_correctPageSize(){
        runBlocking {
            enqueueMockResponse("newsResponse.json")
            val responseBody=service.getTopHeadlines("us",1).body()
            val articleList=responseBody!!.articles
            assertThat(articleList.size).isEqualTo(20)
        }
    }

    @Test
    fun getTopHeadlines_recievedResponse_correctContent(){
        runBlocking {
            enqueueMockResponse("newsResponse.json")
            val responseBody=service.getTopHeadlines("us",1).body()
            val articleList=responseBody!!.articles
            val article=articleList[0]
            assertThat(article.author).isEqualTo("")//check the content
        }
    }

    @After
    fun tearDown() {    //method annotated with after runs after the test
        server.shutdown()  //we dont need to start server as when we call the server functions it automatically starts...but we have to write codes to manually shut down the server
    }
}