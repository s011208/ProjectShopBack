package yhh.com.project.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PageEntity (
    @field:[PrimaryKey ColumnInfo(name = "page")]
    val page: Long = 0,

    @field:[ColumnInfo(name = "since")]
    val since: Long = 0
)