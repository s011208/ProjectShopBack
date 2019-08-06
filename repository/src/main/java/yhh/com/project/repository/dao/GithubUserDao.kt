package yhh.com.project.repository.dao

import androidx.room.*
import io.reactivex.Single
import yhh.com.project.repository.entity.GithubUserEntity

@Dao
interface GithubUserDao {

    @Update
    fun update(entity: GithubUserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entities: List<GithubUserEntity>)

    @Query("SELECT * FROM `GithubUserEntity` WHERE `since` = :since order by `_id`")
    fun getAllUsers(since: Long): Single<List<GithubUserEntity>>

    @Query("SELECT * FROM `GithubUserEntity` WHERE `login` = :login")
    fun getUser(login: String): Single<List<GithubUserEntity>>
}