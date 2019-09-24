package hazae41.minecraft.kutils.bungee

import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS

fun BungeePlugin.cancelTasks() = proxy.scheduler.cancel(this)

fun BungeePlugin.schedule(
    delay: Long = 0,
    period: Long = 0,
    unit: TimeUnit = SECONDS,
    callback: BungeeTask.() -> Unit
): BungeeTask = let { plugin ->
    lateinit var task: BungeeTask
    fun f() = task.callback()
    task = proxy.scheduler.run {
        when {
            period > 0 -> schedule(plugin, ::f, delay, period, unit)
            delay > 0 -> schedule(plugin, ::f, delay, unit)
            else -> schedule(plugin, ::f, 0, unit)
        }
    }
    return task
}