package com.example.salesmansroute.mainphase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.salesmansroute.R
import com.example.salesmansroute.backendphase.Tadabees

/*
* في هذه الدالة عده متغيرات اساسية نستخدمها لأجل توليد شاشة التحكم
* بالاضافة استخدام هذه المتغيرات في البرمجه ايضا
* 1-modifier يستخدم لاغراض التصميم والترتيب كونه العنصر الاساسي في الشاشة
* 2-حجم التشعب والذي سيحوي على النسبة المئوية التي سنعمل عليها
* 3-دالة تستخدم لفرض التغيرات
* 4-معدل الطفرات الوراثية
* 5-ومقدار التغير فيها كدالة
* 6- متغير لأستخدامه في الشرط سواء سوف ينفذ او لا
* 7- دالة تشغيل
* 8-دالة تنظيف
* */
@Composable
fun PanelDeConfiguración(
    modifier: Modifier,
    tamañoDePoblación: String,
    onPopChange: (String) -> Unit,
    tasaDeMutacion: String,
    onMutationChange: (String) -> Unit,
    estaEju: Boolean,
    encender: () -> Unit,
    limpiear: () -> Unit,
    borrar: () -> Unit
) {
    val contexto = LocalContext.current
    val db = remember { Tadabees.obtDataBase(contexto) }
    val listaDeHistoria by db.rutaDeEntidad().obtTodos().collectAsState(initial = emptyList())
    Column(
        modifier
            .fillMaxHeight()
            .padding(top = 23.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = stringResource(R.string.ec), fontWeight = FontWeight.Bold)
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            value = tamañoDePoblación, onValueChange = onPopChange,
            label = { Text(text = stringResource(R.string.población)) }
        )
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            value = tasaDeMutacion, onValueChange = onMutationChange,
            label = { Text(text = stringResource(R.string.mutacion)) }
        )
        Button(
            onClick = encender,
            modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (estaEju) Color.Red else Color.Green
            )
        ) {
            Text(if (estaEju) "Parar" else "Empezar")


        }
        Button(onClick = limpiear, modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.limpiear))
        }
        Button(onClick = borrar,modifier.fillMaxWidth()
        , colors = ButtonDefaults.buttonColors(containerColor = Red)) {
            Text(text = stringResource(R.string.borrar))
        }
        Text(
            "Resultados: ",
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
            style = MaterialTheme.typography.titleMedium
        )


      LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(bottom = 8.dp)
        ) {
            items(listaDeHistoria) { registro ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                )
                {
                    Text("Distancia: ${"%.2f".format(registro.distancia)}",modifier = Modifier.padding(end = 8.dp))
                    Text("Ciudades : ${registro.cuantoCiudades}", color = Color.Magenta)
                }
            }
        }
    }
}
