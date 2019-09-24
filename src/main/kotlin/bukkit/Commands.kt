package hazae41.minecraft.kutils.bukkit

fun BukkitPlugin.command(
    name: String, permission: String? = null, vararg aliases: String,
    executor: BukkitPluginCommand.(BukkitSender, Array<String>) -> Unit
) = getCommand(name)!!.also {
    it.aliases = aliases.toList()
    it.setExecutor { sender, _, _, args ->
        it.executor(sender, args)
        true
    }
    it.permission = permission ?: return@also
}

fun BukkitPlugin.command(
    name: String, permission: String? = null, vararg aliases: String,
    executor: BukkitSender.(Array<String>) -> Unit
) = command(name, permission, *aliases) { sender, args -> sender.executor(args) }