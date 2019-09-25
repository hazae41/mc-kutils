package hazae41.minecraft.kutils.bukkit

inline fun <reified T : BukkitEvent> BukkitPlugin.listen(
    priority: BukkitEventPriority = BukkitEventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline callback: (T) -> Unit
) = server.pluginManager.registerEvent(
    T::class.java, object : BukkitListener {},
    priority, { _, it -> if (it is T) callback(it) },
    this, ignoreCancelled
)