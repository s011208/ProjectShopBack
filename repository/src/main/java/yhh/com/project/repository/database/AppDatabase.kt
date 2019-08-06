package yhh.com.project.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import yhh.com.project.repository.dao.GithubUserDao
import yhh.com.project.repository.dao.PageEntityDao
import yhh.com.project.repository.entity.GithubUserEntity
import yhh.com.project.repository.entity.PageEntity


@Database(entities = [GithubUserEntity::class, PageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun githubUserDao(): GithubUserDao
    abstract fun pageEntityDao(): PageEntityDao
}