@file:JvmName("Kotlin4Bungee")
@file:JvmMultifileClass

package hazae41.minecraft.kotlin.bungee

import hazae41.minecraft.kotlin.catch
import hazae41.minecraft.kotlin.isNewerThan
import hazae41.minecraft.kotlin.spiget
import hazae41.minecraft.kotlin.textOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatColor.LIGHT_PURPLE
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.event.PostLoginEvent
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
    task = {
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
    }()
    return task
}

fun BungeePlugin.cancelTasks() = proxy.scheduler.cancel(this)

// UPDATES
@JvmOverloads
fun BungeePlugin.update(
        id: Int,
        color: ChatColor = LIGHT_PURPLE,
        permission: String = "rhaz.update"
) = catch<Exception>(::warning) {
    GlobalScope.launch {
        val new = spiget(id)
        ?: throw Exception("Could not retrieve latest version")

        val old = description.version

        if (!(new isNewerThan old)) return@launch

        val url = "https://www.spigotmc.org/resources/$id"
        val message = textOf(
            "An update is available for ${description.name} ($old -> $new): $url"
        ).apply {
            this.color = color
            clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url)
        }

        schedule { proxy.console.msg(message) }

        listen<PostLoginEvent> {
            if (it.player.hasPermission(permission))
                it.player.msg(message)
        }
    }
}



