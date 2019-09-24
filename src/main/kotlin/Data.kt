package hazae41.minecraft.kutils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

val currentDate: String get() = SimpleDateFormat("MMM dd yyyy HH:mm:ss").format(Date())

fun String.toTimeWithUnit(): Pair<Long, TimeUnit> {
    val split = split(" ")
    val time = split[0].toLongOrNull()
        ?: error("Cannot convert ${split[0]} to integer")
    val unit = split[1].toTimeUnit()
        ?: error("Cannot convert ${split[1]} to a time unit")
    return Pair(time, unit)
}

fun String.toTimeUnit(default: TimeUnit? = null) =
    when (toLowerCase()) {
        "seconds", "second", "sec", "s" -> TimeUnit.SECONDS
        "minutes", "minute", "min", "m" -> TimeUnit.MINUTES
        "hours", "hour", "h" -> TimeUnit.HOURS
        "days", "day", "d" -> TimeUnit.DAYS
        else -> default
    }
