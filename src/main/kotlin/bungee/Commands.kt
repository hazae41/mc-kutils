package hazae41.minecraft.kutils.bungee

fun BungeeSender.execute(cmd: String) = proxy.pluginManager.dispatchCommand(this, cmd)

fun BungeePlugin.command(
    name: String,
    permission: String? = null,
    vararg aliases: String,
    callback: BungeeCommand.(BungeeSender, Array<String>) -> Unit
) = object : BungeeCommand(name, permission, *aliases) {
    override fun execute(sender: BungeeSender, args: Array<String>) = callback(sender, args)
}.also {
    proxy.pluginManager.registerCommand(this, it)
}

fun BungeePlugin.command(
    name: String,
    permission: String? = null,
    vararg aliases: String,
    callback: BungeeSender.(Array<String>) -> Unit
) = command(name, permission, *aliases) { sender, args -> sender.callback(args) }