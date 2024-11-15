package s.m.mota.sporteventsapp.retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import s.m.mota.sporteventsapp.models.SportCategory

interface ApiInterface {
    @GET("sports")
    suspend fun getSports(): List<SportCategory>?
}

object SportEventsApi {
    private const val BASE_URL = "https://618d3aa7fe09aa001744060a.mockapi.io/api/"


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    val gson =
        GsonBuilder().registerTypeAdapter(SportCategory::class.java, SportCategoryDeserializer())
            .create()

    private val apiService: ApiInterface by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
            .create(ApiInterface::class.java)
    }

    suspend fun getSportCategories(): List<SportCategory>? {
        return apiService.getSports()
    }
}