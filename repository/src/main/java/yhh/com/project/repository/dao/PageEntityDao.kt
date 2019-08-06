package yhh.com.project.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import yhh.com.project.repository.entity.PageEntity

@Dao
interface PageEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: PageEntity)

    @Query("SELECT * FROM `PageEntity` WHERE `page` = :page")
    fun getSince(page: Long): Single<List<PageEntity>>
}