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
                .doOnSuccess { Timber.v("getAllUsers from local, size: ${it.size}, page: $page, since: $since") },
            githubApi.getAllUsers(since, itemPerPage)
                .doOnSuccess { Timber.v("done") }
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

                    if (responseBody != null) {
                        responseBody.forEach { entity ->
                            entity.since = since
                        }
                        database.githubUserDao().insertAll(responseBody)
                        return@map responseBody!!
                    } else {
                        return@map ArrayList<GithubUserEntity>()
                    }
                }
                .doOnSuccess { Timber.v("getAllUsers from remote, size: ${it.size}, page: $page, since: $since") }
                .onErrorReturn {
                    Timber.w(it, "failed to getAllUsers from remote")
                    return@onErrorReturn ArrayList()
                }
        ).filter { it.isNotEmpty() }.firstOrError()
    }

    fun getUser(login: String): Single<GithubUserEntity> {
        return Single.concat(
            database.githubUserDao().getUser(login)
                .doOnSuccess { Timber.v("getUser from local, size: ${it.size}, item: ${it[0]}") },
            githubApi.getSingleUser(login)
                .timeout(10, TimeUnit.SECONDS)
                .doOnSuccess {
                    Timber.v("getUser from remote, result: $it, login: $login")
                    it.hasLoadDetail = true
                    database.githubUserDao().updateDetail(
                        name = it.name ?: "",
                        bio = it.bio ?: "",
                        location = it.location ?: "",
                        blog = it.blog ?: "",
                        login = login
                    )
                }.map { ArrayList<GithubUserEntity>().apply { add(it) } }
        )
            .filter { it.isNotEmpty() && it[0].hasLoadDetail }
            .map { it[0] }
            .firstOrError()
    }

    /**
     * return -1 if cannot find page result
     */
    fun getSince(page: Long): Single<Long> {
        return if (page == 0L) {
            Single.just(0)
        } else {
            database.pageEntityDao()
                .getSince(page)
                .map { it[0].since }
                .onErrorReturn {
                    Timber.w(it, "failed to get since by page: $page")
                    return@onErrorReturn -1
                }
        }
    }
}