package com.example.salesmansroute.backendphase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity ("Historia")
data class Entidad(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val generacion: Int ,
    val distancia : Double ,
    val cuantoCiudades: Int,
    val fechaDeSistema: Long = System.currentTimeMillis()

)