package io.alexeychurchill.clown.library.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "user_directories")
data class RoomDirectory(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "path")
    val path: String,

    @ColumnInfo(name = "alias_title")
    val aliasTitle: String?,

    @ColumnInfo(name = "created_at")
    val addedAt: LocalDateTime,

    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime
)
