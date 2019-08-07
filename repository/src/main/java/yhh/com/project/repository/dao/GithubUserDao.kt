package yhh.com.project.repository.dao

import androidx.room.*
import io.reactivex.Single
import yhh.com.project.repository.entity.GithubUserEntity

@Dao
interface GithubUserDao {

    @Query("UPDATE `GithubUserEntity` SET `name`=:name, `bio`=:bio, `location`=:location, `blog`=:blog, `has_load_detail`=1 WHERE `login`=:login")
    fun updateDetail(name: String = "", bio: String = "", location: String = "", blog: String = "", login: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entities: List<GithubUserEntity>)

    @Query("SELECT * FROM `GithubUserEntity` WHERE `since` = :since order by `_id`")
    fun getAllUsers(since: Long): Single<List<GithubUserEntity>>

    @Query("SELECT * FROM `GithubUserEntity` WHERE `login` = :login")
    fun getUser(login: String): Single<List<GithubUserEntity>>
}