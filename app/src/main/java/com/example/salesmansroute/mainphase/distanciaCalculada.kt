package com.example.salesmansroute.mainphase

fun distanciaCalculada(ruta: List<Int>, ciudades: List<Ciudades>): Double {
    var d = 0.0
    for (i in 0 until ruta.size - 1) {
        val u1 = ciudades[ruta[i]]
        val u2 = ciudades[ruta[i + 1]]
        d += Math.sqrt(
            Math.pow((u1.x - u2.x).toDouble(), 2.0) +
                    Math.pow((u1.y - u2.y).toDouble(), 2.0)
        )
    }
    return d
}