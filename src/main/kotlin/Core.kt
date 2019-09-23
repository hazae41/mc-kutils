package hazae41.minecraft.kutils

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

val currentDate: String get() = SimpleDateFormat("MMM dd yyyy HH:mm:ss").format(Date())

operator fun File.get(key: String) = File(this, key)

val String.lowerCase get() = toLowerCase()

fun <T> T.not(other: T) = takeUnless { it == other }