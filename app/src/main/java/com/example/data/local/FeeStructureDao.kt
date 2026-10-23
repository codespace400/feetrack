package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FeeStructureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeeStructureDao {
  @Query("SELECT * FROM fee_structures ORDER BY classLevel ASC, category ASC")
  fun getAllFeeStructuresFlow(): Flow<List<FeeStructureEntity>>

  @Query("SELECT * FROM fee_structures WHERE classLevel = :classLevel AND term = :term")
  fun getFeeStructuresForClassAndTermFlow(classLevel: String, term: String): Flow<List<FeeStructureEntity>>

  @Query("SELECT * FROM fee_structures WHERE classLevel = :classLevel AND term = :term")
  suspend fun getFeeStructuresForClassAndTerm(classLevel: String, term: String): List<FeeStructureEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFeeStructure(item: FeeStructureEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertFeeStructures(items: List<FeeStructureEntity>)

  @Update
  suspend fun updateFeeStructure(item: FeeStructureEntity)

  @Query("DELETE FROM fee_structures WHERE id = :id")
  suspend fun deleteFeeStructureById(id: Long)

  @Query("SELECT COUNT(*) FROM fee_structures")
  suspend fun getCount(): Int
}
