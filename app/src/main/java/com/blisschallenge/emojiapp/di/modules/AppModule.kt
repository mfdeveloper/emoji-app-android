package com.blisschallenge.emojiapp.di.modules

import android.content.Context
import androidx.room.Room
import com.blisschallenge.emojiapp.models.database.AppDatabase
import com.blisschallenge.emojiapp.models.database.dao.GithubDao
import com.blisschallenge.emojiapp.models.repositories.EmojisRepository
import com.blisschallenge.emojiapp.models.repositories.ProfileRepository
import com.blisschallenge.emojiapp.models.api.GitHubService
import com.blisschallenge.emojiapp.models.api.converters.ListConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val baseUrl = "https://api.github.com"

    /**
     * Warning: I couldn't register a specific TypeTokens here, because
     * the emojis requests returns a json: {"[emojiName]": "[emojiUrl]"}
     *
     * When tried to do this, got the Gson Expected BEGIN_ARRAY but was BEGIN_OBJECT
     *
     *
     * See [CustomTypeAdapter gist](https://gist.github.com/cmelchior/1a97377df0c49cd4fca9)
     *
     * See also [Gson Expected BEGIN_ARRAY but was BEGIN_OBJECT](https://programmersought.com/article/36271114478/)
     *
     * See also [How to use TypeToken + generics with Gson in Kotlin](https://stackoverflow.com/questions/33381384/how-to-use-typetoken-generics-with-gson-in-kotlin)
     *
     * TODO: Consider implement a custom TypeAdapter insteadof JsonDeserializer.
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {

        return GsonBuilder()
            .registerTypeAdapter(List::class.java, ListConverterFactory())
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create()
    }

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(client)
            .build()
    }

    @Provides
    fun provideGitHubService (retrofit: Retrofit): GitHubService = retrofit.create(GitHubService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {

        if (AppDatabase.CLEAR_FIRST) {
            appContext.deleteDatabase(AppDatabase.DATABASE_NAME)
        }

        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
         .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: AppDatabase) = db.githubDao()

    @Singleton
    @Provides
    fun provideRepository(service: GitHubService, dao: GithubDao) = EmojisRepository(service, dao)

    @Singleton
    @Provides
    fun provideProfileRepository(db: AppDatabase, service: GitHubService, dao: GithubDao) = ProfileRepository(db, service, dao)
}