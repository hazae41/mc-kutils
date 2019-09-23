package hazae41.minecraft.kutils.bungee

import java.util.concurrent.TimeUnit

fun BungeePlugin.cancelTasks() = proxy.scheduler.cancel(this)

fun BungeePlugin.schedule(
    async: Boolean = false,
    delay: Long? = null,
    period: Long? = null,
    unit: TimeUnit? = null,
    callback: BungeeTask.() -> Unit
): BungeeTask {
    lateinit var task: BungeeTask
    task = run {
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
    }
    return task
}