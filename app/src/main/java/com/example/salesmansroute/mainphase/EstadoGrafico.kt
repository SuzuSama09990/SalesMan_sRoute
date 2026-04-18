package com.example.salesmansroute.mainphase
/*

هذا الداتا كلاس خاص بالحالة الجغرافية فهنا مثلناه بالرسم يعني كحالة كرافيكيه
1-حندخل مجموعة مدن بشكل لسته علمود يكونن بالرسم
2- حنختار افضل طريق رابط بين اقرب نقطين ويكون المجموع يساوي لأقصر طريق
3- عدد الاجيال طبعا هنا خلينها كهارد كود اقصى شي يوصل 999
4-نقيس المسافة علمود نحدد اقصر طريق
5- هنا حالة الشرط اذا كان البرنامج شغال او لا
*/
data class EstadoGrafico(
    val ciudades : List<Ciudades> = emptyList(),
    val laMejorRuta : List<Int> = emptyList(),
    val generación :Int = 0 ,
    val distancia : Double = 0.0,
    val estáEjecutando : Boolean = false
)