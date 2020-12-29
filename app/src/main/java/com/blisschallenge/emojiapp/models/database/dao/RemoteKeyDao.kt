package com.blisschallenge.emojiapp.models.database.dao

import androidx.room.*
import com.blisschallenge.emojiapp.models.entities.RemoteKey

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKeys: List<RemoteKey>?)

    @Query("SELECT * FROM remote_keys WHERE repoId == :repoId")
    suspend fun findRemoteKeyBy(repoId: String): RemoteKey

    @Query("DELETE FROM remote_keys WHERE repoId == :repoId")
    suspend fun deleteBy(repoId: String)

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
