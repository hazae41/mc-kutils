@file:JvmName("Kotlin4Bungee")
@file:JvmMultifileClass

package hazae41.minecraft.kotlin.bungee

import hazae41.minecraft.kotlin.*
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import java.io.FileWriter
import java.io.PrintWriter

typealias BungeePlugin = net.md_5.bungee.api.plugin.Plugin
typealias BungeeSender = net.md_5.bungee.api.CommandSender
typealias BungeeEvent = net.md_5.bungee.api.plugin.Event
typealias BungeeEventPriority = net.md_5.bungee.event.EventPriority
typealias BungeeListener = net.md_5.bungee.api.plugin.Listener
typealias BungeeEventHandler = net.md_5.bungee.event.EventHandler
typealias BungeeConfiguration = net.md_5.bungee.config.Configuration
typealias BungeeYaml = net.md_5.bungee.config.YamlConfiguration
typealias BungeeConfigurationProvider = net.md_5.bungee.config.ConfigurationProvider
typealias BungeeCommand = net.md_5.bungee.api.plugin.Command
typealias BungeeTask = net.md_5.bungee.api.scheduler.ScheduledTask

typealias BungeeConfig = Config
typealias BungeeConfigFile = ConfigFile
typealias BungeeConfigSection = ConfigSection

fun BungeePlugin.info(msg: String) = logger.info(msg.replace("&", "§"))
fun BungeePlugin.info(ex: Exception) {
    ex.message?.also(::info)
}

fun BungeePlugin.warning(msg: String) = logger.warning(msg.replace("&", "§"))
fun BungeePlugin.warning(ex: Exception) {
    ex.message?.also(::warning)
}

fun BungeePlugin.severe(msg: String) = logger.severe(msg.replace("&", "§"))
fun BungeePlugin.severe(ex: Exception) {
    ex.message?.also(::severe)
}

fun BungeePlugin.logToFile(ex: Exception) = logToFile { ex.printStackTrace(this) }
fun BungeePlugin.logToFile(msg: String) = logToFile { println(msg) }
val BungeePlugin.logFile
    get() = dataFolder["log.txt"].apply { if (!exists()) createNewFile() }

fun BungeePlugin.logToFile(action: PrintWriter.() -> Unit) =
        PrintWriter(FileWriter(logFile, true), true)
                .apply { print(currentDate); action() }.close()


fun BungeeSender.msg(msg: String) = msg(textOf(msg))
fun BungeeSender.msg(text: TextComponent) = sendMessage(text)
fun BungeeSender.msg(ex: Exception) {
    ex.message?.also(::msg)
}

fun BungeeSender.execute(cmd: String) = ProxyServer.getInstance().pluginManager.dispatchCommand(this, cmd)