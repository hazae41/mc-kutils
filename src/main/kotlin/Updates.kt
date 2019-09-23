package hazae41.minecraft.kutils

import com.google.gson.JsonParser
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

fun spiget(id: Int): String? = try {
    val base = "https://api.spiget.org/v2/resources/"
    val conn = URL("$base$id/versions?size=100").openConnection()
    val json = InputStreamReader(conn.inputStream).let { JsonParser().parse(it).asJsonArray }
    json.last().asJsonObject["name"].asString
} catch (e: IOException) {
    null
}

infix fun String.isNewerThan(v: String) = false.also {
    val s1 = split('.')
    val s2 = v.split('.')
    for (i in 0..Math.max(s1.size, s2.size)) {
        if (i !in s1.indices) return false
        if (i !in s2.indices) return true
        if (s1[i].toInt() > s2[i].toInt()) return true
        if (s1[i].toInt() < s2[i].toInt()) return false
    }
}
