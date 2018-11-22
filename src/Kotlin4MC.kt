@file:JvmName("Kotlin4MC")

package fr.rhaz.minecraft.kotlin

import com.google.gson.JsonParser
import net.md_5.bungee.api.chat.TextComponent
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.*

val currentDate: String get() = SimpleDateFormat("MMM dd yyyy HH:mm:ss").format(Date())

operator fun File.get(key: String) = File(this, key)

val String.lowerCase get() = toLowerCase()

fun <T> T.eq(other: T) = takeIf { it == other }
fun <T> T.not(other: T) = takeUnless { it == other }

fun ex(msg: String) = Exception(msg)

inline fun <reified T : Exception, reified U : Any> catch(
        err: (T) -> U,
        run: () -> U
): U = try {
    run()
} catch (ex: Exception) {
    if (ex is T) err(ex) else throw ex
}

inline fun <reified T : Exception> catch(
        err: (T) -> Unit = { it.printStackTrace() },
        run: () -> Unit
): Unit = catch<T, Unit>(err, run)

inline fun <reified T : Exception, reified U : Any> catch(
        default: U,
        run: () -> U
): U = try {
    run()
} catch (ex: Exception) {
    if (ex is T) default else throw ex
}

infix fun <T> Boolean.then(param: T): T? = if (this) param else null

fun String.toTimeWithUnit(): Pair<Long, TimeUnit> {
    val split = split(" ")
    val time = split[0].toLongOrNull()
    ?: throw ex("Cannot convert ${split[0]} to integer")
    val unit = split[1].toTimeUnit()
    ?: throw ex("Cannot convert ${split[1]} to a time unit")
    return Pair(time, unit)
}

fun String.toTimeUnit(default: TimeUnit? = null) =
    when(this.lowerCase) {
        "seconds", "second", "sec", "s" -> SECONDS
        "minutes", "minute", "min", "m" -> MINUTES
        "hours", "hour", "h" -> HOURS
        "days", "day", "d" -> DAYS
        else -> default
    }

// MESSAGING
fun textOf(string: String) = TextComponent(string.replace("&", "§"))

// UPDATES CHECKER
fun spiget(id: Int): String? = try {
    val base = "https://api.spiget.org/v2/resources/"
    val conn = URL("$base$id/versions?size=100").openConnection()
    val json = InputStreamReader(conn.inputStream).let { JsonParser().parse(it).asJsonArray }
    json.last().asJsonObject["name"].asString
} catch (e: IOException) { null }

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
