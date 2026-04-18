package com.example.salesmansroute.backendphase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface rutaDeEntidad {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(info: Entidad): Long

    @Query("Select * from Historia order by fechaDeSistema DESC")
    fun obtTodos(): Flow<List<Entidad>>

    @Query("Delete from Historia")
    suspend fun BorrarTodosResultados()
}