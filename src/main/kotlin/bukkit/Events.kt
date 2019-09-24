package hazae41.minecraft.kutils.bukkit

import hazae41.minecraft.kotlin.bukkit.BukkitEvent
import hazae41.minecraft.kotlin.bukkit.BukkitEventPriority
import hazae41.minecraft.kotlin.bukkit.BukkitListener
import hazae41.minecraft.kotlin.bukkit.BukkitPlugin

inline fun <reified T : BukkitEvent> BukkitPlugin.listen(
    priority: BukkitEventPriority = BukkitEventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    crossinline callback: (T) -> Unit
) = server.pluginManager.registerEvent(
    T::class.java, object : BukkitListener {},
    priority, { _, it -> if (it is T) callback(it) },
    this, ignoreCancelled
)