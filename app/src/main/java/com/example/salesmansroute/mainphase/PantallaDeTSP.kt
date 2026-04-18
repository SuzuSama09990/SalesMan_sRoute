package com.example.salesmansroute.mainphase

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.salesmansroute.backendphase.Entidad
import com.example.salesmansroute.backendphase.Tadabees
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PantallaDeTSP(
    modifier: Modifier
) {
    //هنا حنضيف القيم الابتدائية لكل متغير
    var ciudades by remember { mutableStateOf(listOf<Ciudades>()) }
    var laMejorRuta by remember { mutableStateOf(listOf<Int>()) }
    var distancia by remember { mutableStateOf(0.0) }
    var generacion by remember { mutableStateOf(0) }
    var estaEjecutando by remember { mutableStateOf(false) }
    var popSize by remember { mutableStateOf("100") }
    var tastDemutacion by remember { mutableStateOf("15") }
    //سكوب للتحكم في الthreads
    //ال job علمود نكدر نوكف البرنامج او ننظفه بدون تداخل معه اذا كان شغال
    val alcance = rememberCoroutineScope()
    var workerJob by remember { mutableStateOf<Job?>(null) }
    val contexto = LocalContext.current
    val salesManRouteDB = remember { Tadabees.obtDataBase(contexto) }
    val registro = Entidad(
        distancia = distancia,
        generacion = generacion,
        cuantoCiudades = ciudades.size
    )
    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    //هنا حنسمح للمستخدم بالنقر علمود يرسم المدن في حال عدم تشغيل البرنامج
                    .weight(1f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            if (!estaEjecutando) {
                                ciudades = ciudades + Ciudades(ciudades.size, offset.x, offset.y)
                                laMejorRuta = ciudades.indices.toList()
                            }
                        }
                    }


        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (laMejorRuta.size > 1) {
                    val ruta = Path().apply {
                        val laPrimeraCiudad = ciudades[laMejorRuta[0]]
                        moveTo(laPrimeraCiudad.x, laPrimeraCiudad.y)
                        for (i in 1 until laMejorRuta.size) {
                            val ciudad = ciudades[laMejorRuta[i]]
                            lineTo(ciudad.x, ciudad.y)
                        }
                        close()
                    }
                    drawPath(ruta, color = Color.Magenta, style = Stroke(width = 5f))
                }
                ciudades.forEach { ciudad ->
                    drawCircle(color = Color.Red, radius = 10f, center = Offset(ciudad.x, ciudad.y))
                }
            }
            Text(
                "Generacion: $generacion | Distancia: ${"%.2f".format(distancia)}",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 32.dp, start = 16.dp, end = 16.dp, top = 16.dp)
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        PanelDeConfiguración(
            modifier = Modifier.width(200.dp),
            tamañoDePoblación = popSize,
            onPopChange = { popSize = it },
            tastDemutacion,
            onMutationChange = { tastDemutacion = it },
            estaEjecutando,
            encender = {
                if (estaEjecutando) {
                    estaEjecutando = false
                    workerJob?.cancel()
                    alcance.launch (Dispatchers.IO){
                        salesManRouteDB.rutaDeEntidad().guardar(registro)
                    }
                } else if (ciudades.size > 2) {
                    estaEjecutando = true
                    workerJob = alcance.launch(Dispatchers.Default) {
                        Log.d("AzizDB","Started")
                        try {
                            ejeGeneticAlgorithm(
                                ciudades,
                                popSize.toIntOrNull() ?: 100,
                                (tastDemutacion.toFloatOrNull() ?: 15f) / 100f,
                            ) { rutaNueva, genNuevo, disNueva ->
                                laMejorRuta = rutaNueva
                                generacion = genNuevo
                                distancia = disNueva
                            }
                            Log.d("AzizDB","ended")




                            withContext(Dispatchers.IO){
                                salesManRouteDB.rutaDeEntidad().guardar(registro)
                                Log.d("AzizDB","dones")

                            }







                        }
                        finally {
                            estaEjecutando = false
                        }
                    }

                }
            },
            limpiear = {
                ciudades = emptyList()
                laMejorRuta = emptyList()
                workerJob?.cancel()
                distancia = 0.0
                generacion = 0
                estaEjecutando = false
            },
            borrar = {
                alcance.launch(NonCancellable) {
                    salesManRouteDB.rutaDeEntidad().BorrarTodosResultados()
                }
            }
        )


    }
}