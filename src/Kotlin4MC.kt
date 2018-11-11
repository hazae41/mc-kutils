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
import java.util.function.BiConsumer
import java.util.function.Consumer

// JAVA COMPAT
val unit = Unit

fun <T> listener(callable: Consumer<T>): Function1<T, Unit> = { t -> callable.accept(t); Unit }
fun <T, U> listener(callable: BiConsumer<T, U>): Function2<T, U, Unit> = { t, u -> callable.accept(t, u); Unit }
fun <T, U, V> listener(callable: TriConsumer<T, U, V>): Function3<T, U, V, Unit> = { t, u, v -> callable.accept(t, u, v); Unit }
@FunctionalInterface
interface TriConsumer<T, U, V> {
    fun accept(t: T, u: U, v: V)

    fun andThen(after: TriConsumer<in T, in U, in V>): TriConsumer<T, U, V> {
        Objects.requireNonNull(after)
        return object : TriConsumer<T, U, V> {
            override fun accept(a: T, b: U, c: V) {
                accept(a, b, c)
                after.accept(a, b, c)
            }
        }
    }
}


// OTHERS
val date: String get() = SimpleDateFormat("MMM dd yyyy HH:mm:ss").format(Date())

operator fun File.get(key: String) = File(this, key)

val String.lowerCase get() = toLowerCase()

fun <T> T.eq(other: T) = takeIf { it == other }
fun <T> T.not(other: T) = takeUnless { it == other }

val Any.unit get() = Unit
val Any.nul get() = null

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

@Deprecated("", ReplaceWith("unitOf(unit, default)"))
fun unit(unit: String, default: TimeUnit = MINUTES) = unitOf(unit, default)

fun unitOf(unit: String, default: TimeUnit = MINUTES) =
        when (unit) {
            "seconds", "second", "sec", "s" -> SECONDS
            "minutes", "minute", "min", "m" -> MINUTES
            "hours", "hour", "h" -> HOURS
            "days", "day", "d" -> DAYS
            else -> default
        }

// MESSAGING
fun text(string: String) = TextComponent(string.replace("&", "§"))

// UPDATES CHECKER
fun spiget(id: Int): String? = try {
    val base = "https://api.spiget.org/v2/resources/"
    val conn = URL("$base$id/versions?size=100").openConnection()
    val json = InputStreamReader(conn.inputStream).let { JsonParser().parse(it).asJsonArray }
    json.last().asJsonObject["name"].asString
} catch (e: IOException) { null }

infix fun String.newerThan(v: String): Boolean {
    val s1 = split('.')
    val s2 = v.split('.')
    for (i in 0..Math.max(s1.size, s2.size)) {
        if (i !in s1.indices) return false
        if (i !in s2.indices) return true
        if (s1[i].toInt() > s2[i].toInt()) return true
        if (s1[i].toInt() < s2[i].toInt()) return false
    }
    return false
}
