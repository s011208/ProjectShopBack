package yhh.com.project.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class GithubUserEntity(
    @field:[PrimaryKey SerializedName("id") ColumnInfo(name = "_id")]
    val userId: Long = -1,

    @field:[SerializedName("login") ColumnInfo(name = "login")]
    val login: String = "",

    @field:[SerializedName("name") ColumnInfo(name = "name")]
    var name: String = "",

    @field:[SerializedName("avatar_url") ColumnInfo(name = "avatar_url")]
    val avatarUrl: String = "",

    @field:[SerializedName("site_admin") ColumnInfo(name = "site_admin")]
    val isSiteAdmin: Boolean = false,

    @field:[SerializedName("bio") ColumnInfo(name = "bio")]
    var bio: String = "",

    @field:[SerializedName("location") ColumnInfo(name = "location")]
    var location: String = "",

    @field:[SerializedName("blog") ColumnInfo(name = "blog")]
    var blog: String = "",

    @field:[ColumnInfo(name = "since")]
    var since: Long = 0,

    @field:[ColumnInfo(name = "has_load_detail")]
    var hasLoadDetail: Boolean = false
)