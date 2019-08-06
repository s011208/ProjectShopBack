package yhh.com.project.repository.repository

import android.net.Uri
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber
import yhh.com.project.repository.BuildConfig
import yhh.com.project.repository.database.AppDatabase
import yhh.com.project.repository.entity.GithubUserEntity
import yhh.com.project.repository.entity.PageEntity
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GithubRepository @Inject constructor(private val database: AppDatabase) {

    private interface GithubApi {
        @GET("/users")
        fun getAllUsers(
            @Query("since") since: Long,
            @Query("per_page") itemPerPage: Int,
            @Query("client_id") clientId: String = BuildConfig.GITHUB_CLIENT_ID,
            @Query("client_secret") clientSecret: String = BuildConfig.GITHUB_CLIENT_SECRET
        ): Single<Response<List<GithubUserEntity>>>

        @GET("/users/{username}")
        fun getSingleUser(
            @Path("username") login: String,
            @Query("client_id") clientId: String = BuildConfig.GITHUB_CLIENT_ID,
            @Query("client_secret") clientSecret: String = BuildConfig.GITHUB_CLIENT_SECRET
        ): Single<GithubUserEntity>
    }

    private val githubApi =
        Retrofit
            .Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(GithubApi::class.java)

    fun getUsers(page: Long = 0L, itemPerPage: Int = 20): Single<List<GithubUserEntity>> {
        return getSince(page)
            .flatMap { since ->
                if (since >= 0) {
                    return@flatMap getUsersInternal(page, since, itemPerPage)
                } else {
                    if (page == 0L) {
                        // first page
                        return@flatMap getUsersInternal(0, 0, itemPerPage)
                    } else {
                        throw IllegalArgumentException("Cannot find since in page: $page")
                    }
                }
            }
    }

    private fun getUsersInternal(page: Long, since: Long, itemPerPage: Int): Single<List<GithubUserEntity>> {
        return Single.concat(
            database.githubUserDao().getAllUsers(since)
                .doOnSuccess { Timber.v("getAllUsers from local, size: ${it.size}") },
            githubApi.getAllUsers(since, itemPerPage)
                .timeout(10, TimeUnit.SECONDS)
                .map {
                    val header = it.headers()
                    val responseBody = it.body()
                    val errorBody = it.errorBody()

                    if (errorBody != null) {
                        Timber.w(errorBody.string())
                        throw Exception(errorBody.string())
                    }

                    if (!header["Link"].isNullOrBlank()) {
                        // save page & since if header can find real=next
                        val links = header["Link"]!!.split(",")
                        links.forEach { link ->
                            if (link.contains("rel=\"next\"", ignoreCase = true)) {
                                var html = link.split(";")[0].trim()
                                html = html.substring(1, html.length - 1)
                                Timber.i("html: $html")
                                val sinceResult: Long = (Uri.parse(html).getQueryParameter("since")?.toLong() ?: -1)
                                if (since >= 0) {
                                    val pageEntity = PageEntity(page + 1, sinceResult)
                                    database.pageEntityDao().insert(pageEntity)
                                    Timber.v("pageEntity: $pageEntity")
                                } else {
                                    Timber.v("Cannot find page entity")
                                }
                            }
                        }
                    }

                    return@map responseBody!!
                }.onErrorReturnItem(ArrayList())
        ).filter { it.isNotEmpty() }.firstOrError()
    }

    fun getUser(entity: GithubUserEntity): Single<List<GithubUserEntity>> {
        return Single.concat(
            database.githubUserDao().getUser(entity.login)
                .doOnSuccess { Timber.v("getUser from locale, size: ${it.size}") },
            githubApi.getSingleUser(entity.login)
                .timeout(10, TimeUnit.SECONDS)
                .doOnSuccess {
                    Timber.v("getUser from remote, result: $it")
                    it.since = entity.since
                    it.hasLoadDetail = true
                    database.githubUserDao().update(it)
                }.map { ArrayList<GithubUserEntity>().apply { add(it) } }
        ).filter { it.isNotEmpty() }.firstOrError()
    }

    fun getSince(page: Long): Single<Long> {
        return database.pageEntityDao()
            .getSince(page)
            .map { it[0].since }
            .onErrorReturn {
                Timber.w(it, "failed to get since by page: $page")
                return@onErrorReturn -1
            }
    }
}