package hazae41.minecraft.kutils

import java.io.File

operator fun File.get(key: String) = File(this, key)

infix fun String.match(other: String) = purified == other.purified

val String.purified get() = toLowerCase().trim()

fun <T> T.not(other: T) = takeUnless { it == other }
fun <T> T.notIn(container: Iterable<T>) = takeUnless { it in container }