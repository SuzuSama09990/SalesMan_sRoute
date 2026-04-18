package com.example.salesmansroute.mainphase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

suspend fun ejeGeneticAlgorithm(
    ciudades: List<Ciudades>,
    popSize : Int,
    tasaDeMutacion : Float,

    onUpdate: (List<Int>, Int, Double ) -> Unit
){
    var generacionActual = 0
    var poblacion = List(popSize){
        ciudades.indices.toList().shuffled()
    }
    while (generacionActual<1000 && currentCoroutineContext().isActive){

        poblacion = poblacion.sortedBy { distanciaCalculada (it, ciudades) }
        val mejorAcutal = poblacion[0]
        val dist = distanciaCalculada(mejorAcutal,ciudades)
        withContext(Dispatchers.Main){
            onUpdate(mejorAcutal,generacionActual,dist)
        }

        delay(20)
        val generacionProximo = mutableListOf<List<Int>>()
        repeat(popSize){
            val parente = poblacion.take(10).random()
            val hijo = mutate(parente,tasaDeMutacion)
            generacionProximo.add(hijo)
        }
        poblacion = generacionProximo
        generacionActual++
    }


}

