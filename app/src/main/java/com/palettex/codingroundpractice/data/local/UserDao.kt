package com.palettex.codingroundpractice.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT login_time FROM login_history ORDER BY login_time DESC LIMIT 1")
    suspend fun getLastLoginTime(): String
}
