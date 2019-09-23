package hazae41.minecraft.kutils.bungee

import net.md_5.bungee.event.EventBus
import java.lang.reflect.Method

inline fun <reified T : BungeeEvent> BungeePlugin.listen(
    priority: Byte = BungeeEventPriority.NORMAL,
    crossinline callback: (T) -> Unit
) {
    val pluginManager = proxy.pluginManager
    val eventBus = pluginManager::class.java.getDeclaredField("eventBus").run {
        isAccessible = true; get(pluginManager) as EventBus
    }

    val byListenerAndPriority = eventBus::class.java.getDeclaredField("byListenerAndPriority").run {
        isAccessible = true; get(eventBus) as HashMap<Class<*>, Map<Byte, Map<Object, Array<Method>>>>
    }

    val priorities = byListenerAndPriority[T::class.java] as? HashMap<Byte, Map<Object, Array<Method>>>
        ?: HashMap<Byte, Map<Object, Array<Method>>>().also { byListenerAndPriority[T::class.java] = it }

    val handlers = priorities[priority] as? HashMap<Object, Array<Method>>
        ?: HashMap<Object, Array<Method>>().also { priorities[priority] = it }

    val listener = object : BungeeListener {
        fun onEvent(it: T) = callback(it)
    }

    handlers[listener as Object] = arrayOf(listener::class.java.getMethod("onEvent", BungeeEvent::class.java))
    eventBus::class.java.getDeclaredMethod("bakeHandlers", Class::class.java).apply {
        isAccessible = true; invoke(eventBus, T::class.java)
    }
    
    proxy.pluginManager.registerListener(this, listener)
}