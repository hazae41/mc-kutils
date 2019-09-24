package hazae41.minecraft.kutils.bukkit

import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS

fun BukkitPlugin.cancelTasks() = server.scheduler.cancelTasks(this)

fun BukkitPlugin.schedule(
    async: Boolean = false,
    delay: Long = 0,
    period: Long = 0,
    unit: TimeUnit = SECONDS,
    callback: BukkitTask.() -> Unit
): BukkitTask = let { plugin ->
    lateinit var task: BukkitTask
    fun f() = task.callback()
    task = server.scheduler.run {
        val sdelay = unit.toSeconds(delay) * 20
        val speriod = unit.toSeconds(period) * 20
        when {
            period > 0 -> {
                when {
                    async -> runTaskTimerAsynchronously(plugin, ::f, sdelay, speriod)
                    else -> runTaskTimer(plugin, ::f, sdelay, speriod)
                }
            }
            delay > 0 -> {
                when {
                    async -> runTaskLaterAsynchronously(plugin, ::f, sdelay)
                    else -> runTaskLater(plugin, ::f, sdelay)
                }
            }
            async -> runTaskAsynchronously(plugin, ::f)
            else -> runTask(plugin, ::f)
        }
    }
    return task
}
