@file:JvmName("Kotlin4Bungee")
@file:JvmMultifileClass

package fr.rhaz.minecraft.kotlin.bungee

import fr.rhaz.minecraft.kotlin.catch
import fr.rhaz.minecraft.kotlin.spiget
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatColor.LIGHT_PURPLE
import java.util.concurrent.TimeUnit

lateinit var kotlinBungee: Kotlin4BungeePlugin

class Kotlin4BungeePlugin : BungeePlugin() {
    init {
        kotlinBungee = this
    }

    override fun onEnable() = update(58015, LIGHT_PURPLE)
}

// SCHEDULE
fun BungeePlugin.schedule(
        async: Boolean = false,
        delay: Long? = null,
        period: Long? = null,
        unit: TimeUnit? = null,
        callback: BungeeTask.() -> Unit
): BungeeTask {
    lateinit var task: BungeeTask
    task =
            if (period != null) {
                var delay = delay ?: 0
                val unit = unit ?: TimeUnit.MILLISECONDS.also { delay *= 50 }
                if (async)
                    proxy.scheduler.schedule(this, {
                        proxy.scheduler.runAsync(this) { task.callback() }
                    }, delay, period, unit)
                else proxy.scheduler.schedule(this, { task.callback() }, delay, period, unit)
            } else if (delay != null) {
                var delay = delay
                val unit = unit ?: TimeUnit.MILLISECONDS.also { delay *= 50 }
                if (async)
                    proxy.scheduler.schedule(this, {
                        proxy.scheduler.runAsync(this) { task.callback() }
                    }, delay, unit)
                else proxy.scheduler.schedule(this, { task.callback() }, delay, unit)
            } else if (async) proxy.scheduler.runAsync(this) { task.callback() }
            else proxy.scheduler.schedule(this, { task.callback() }, 0, TimeUnit.MILLISECONDS)
    return task
}

fun BungeePlugin.cancelTasks() = proxy.scheduler.cancel(this)

// UPDATES
@JvmOverloads
fun BungeePlugin.update(
        id: Int,
        color: ChatColor = LIGHT_PURPLE,
        permission: String = "rhaz.update"
) = catch<Exception>(::logToFile) {
    spiget(id)
}



