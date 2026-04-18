package com.example.salesmansroute.backendphase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Entidad::class], version = 1)
abstract class Tadabees: RoomDatabase() {
    abstract fun rutaDeEntidad() : rutaDeEntidad
    companion object{
        @Volatile
        private var INST : Tadabees? =null
        fun obtDataBase(context: Context): Tadabees{
            return INST ?: synchronized(this){
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    Tadabees::class.java,
                    "salesManRoute_db"
                ).build()
                INST = inst
                inst
            }
        }
    }
}