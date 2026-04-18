package com.example.salesmansroute.mainphase

import java.util.Collections


fun mutate(ruta:List<Int>,tasa:Float): List<Int>{
    if(Math.random()>tasa)return ruta
    val mutado = ruta.toMutableList()
    val a = (0 until ruta.size).random()
    var b = (0 until ruta.size).random()
    while(a == b && ruta.size>1){
        b = (0 until ruta.size ).random()
    }
    Collections.swap(mutado,a,b)
    return mutado
}