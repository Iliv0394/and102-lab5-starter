package com.codepath.campgrounds

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.campgrounds.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Headers

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "CampgroundsMain"
private val API_KEY = BuildConfig.API_KEY

private val CAMPGROUNDS_URL =
    "https://developer.nps.gov/api/v1/campgrounds?api_key=$API_KEY"

class MainActivity : AppCompatActivity() {

    private val campgrounds = mutableListOf<Campground>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var campgroundAdapter: CampgroundAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        campgroundAdapter = CampgroundAdapter(this, campgrounds)

        binding.campgrounds.layoutManager = LinearLayoutManager(this)
        binding.campgrounds.adapter = campgroundAdapter

        // Get saved campgrounds from database
        lifecycleScope.launch {
            (application as CampgroundApplication)
                .db
                .campgroundDao()
                .getAll()
                .collect { savedCampgrounds ->

                    campgrounds.clear()

                    savedCampgrounds.forEach {
                        campgrounds.add(
                            Campground(
                                it.name,
                                it.description,
                                it.latLong,
                                listOf(CampgroundImage(it.imageUrl, null))
                            )
                        )
                    }

                    campgroundAdapter.notifyDataSetChanged()
                }
        }

        getCampgrounds()
    }

    private fun getCampgrounds() {

        val client = AsyncHttpClient()

        client.get(
            CAMPGROUNDS_URL,
            object : JsonHttpResponseHandler() {

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    Log.e(TAG, "Failed to fetch campgrounds: $statusCode")
                }

                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JSON
                ) {

                    try {
                        val response = createJson().decodeFromString(
                            CampgroundResponse.serializer(),
                            json.jsonObject.toString()
                        )

                        response.data?.let { list ->

                            lifecycleScope.launch(Dispatchers.IO) {

                                val dao =
                                    (application as CampgroundApplication)
                                        .db
                                        .campgroundDao()

                                dao.deleteAll()

                                val savedList = list.map {
                                    CampgroundEntity(
                                        name = it.name,
                                        description = it.description,
                                        latLong = it.latLong,
                                        imageUrl = it.imageUrl
                                    )
                                }

                                dao.insertAll(savedList)
                            }
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "Error: $e")
                    }
                }
            }
        )
    }
}