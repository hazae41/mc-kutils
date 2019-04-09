@file:JvmName("Kotlin4Bungee")
@file:JvmMultifileClass

package hazae41.minecraft.kotlin.bungee

import net.md_5.bungee.event.EventBus
import java.lang.reflect.Method

// COMMANDS
fun BungeePlugin.command(
        name: String,
        permission: String,
        vararg aliases: String,
        callback: BungeeCommand.(BungeeSender, Array<String>) -> Unit
) = object : BungeeCommand(name, permission, *aliases) {
    override fun execute(sender: BungeeSender, args: Array<String>) = callback(sender, args)
}.also { proxy.pluginManager.registerCommand(this, it) }

fun BungeePlugin.command(
        name: String,
        callback: BungeeCommand.(BungeeSender, Array<String>) -> Unit
) = object : BungeeCommand(name) {
    override fun execute(sender: BungeeSender, args: Array<String>) = callback(sender, args)
}.also { proxy.pluginManager.registerCommand(this, it) }

// --- SENDER AS RECEIVER ---
fun BungeePlugin.command(
        name: String,
        permission: String,
        vararg aliases: String,
        callback: BungeeSender.(Array<String>) -> Unit
) = command(name, permission, *aliases) { sender, args -> sender.callback(args) }

fun BungeePlugin.command(
        name: String,
        callback: BungeeSender.(Array<String>) -> Unit
) = command(name) { sender, args -> sender.callback(args) }

// EVENTS
@JvmOverloads
inline fun <reified T : BungeeEvent> BungeePlugin.listen(
        priority: Byte = BungeeEventPriority.NORMAL,
        crossinline callback: (T) -> Unit
) {
    val pm = proxy.pluginManager
    val pmc = pm::class.java
    val bus = pmc.getDeclaredField("eventBus").run {
        isAccessible = true; get(pm) as EventBus
    }

    val busc = bus::class.java
    val bLaP = busc.getDeclaredField("byListenerAndPriority").run {
        isAccessible = true; get(bus) as HashMap<Class<*>, Map<Byte, Map<Object, Array<Method>>>>
    }

    val priorities = bLaP[T::class.java] as? HashMap<Byte, Map<Object, Array<Method>>>
            ?: HashMap<Byte, Map<Object, Array<Method>>>().also { bLaP[T::class.java] = it }

    val handlers = priorities[priority] as? HashMap<Object, Array<Method>>
            ?: HashMap<Object, Array<Method>>().also { priorities[priority] = it }

    val listener = object : BungeeListener {
        fun onEvent(it: T) = callback(it)
    }

    handlers[listener as Object] = arrayOf(listener::class.java.getMethod("onEvent", BungeeEvent::class.java))
    busc.getDeclaredMethod("bakeHandlers", Class::class.java).apply {
        isAccessible = true; invoke(bus, T::class.java)
    }
    proxy.pluginManager.registerListener(this, listener)
}