@file:JvmName("Kotlin4Bukkit")
@file:JvmMultifileClass

package hazae41.minecraft.kotlin.bukkit

import hazae41.minecraft.kotlin.currentDate
import hazae41.minecraft.kotlin.get
import hazae41.minecraft.kotlin.textOf
import hazae41.minecraft.kotlin.translateColorCode
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import java.io.FileWriter
import java.io.PrintWriter

typealias BukkitPlugin = org.bukkit.plugin.java.JavaPlugin
typealias BukkitPluginCommand = org.bukkit.command.PluginCommand
typealias BukkitSender = org.bukkit.command.CommandSender
typealias BukkitEvent = org.bukkit.event.Event
typealias BukkitListener = org.bukkit.event.Listener
typealias BukkitEventPriority = org.bukkit.event.EventPriority
typealias BukkitEventHandler = org.bukkit.event.EventHandler
typealias BukkitConfiguration = org.bukkit.configuration.file.YamlConfiguration
typealias BukkitCommandExecutor = org.bukkit.command.CommandExecutor
typealias BukkitConfigurationSection = org.bukkit.configuration.ConfigurationSection
typealias BukkitPlayer = org.bukkit.entity.Player

typealias BukkitConfig = Config
typealias BukkitConfigFile = ConfigFile
typealias BukkitConfigSection = ConfigSection

fun BukkitPlugin.info(msg: String) = logger.info(msg.replace("&", "§"))
fun BukkitPlugin.info(ex: Exception) {
    ex.message?.also(::info)
}

fun BukkitPlugin.warning(msg: String) = logger.warning(msg.replace("&", "§"))
fun BukkitPlugin.warning(ex: Exception) {
    ex.message?.also(::warning)
}

fun BukkitPlugin.severe(msg: String) = logger.severe(msg.replace("&", "§"))
fun BukkitPlugin.severe(ex: Exception) {
    ex.message?.also(::severe)
}

fun BukkitPlugin.error(ex: Exception){ severe(ex.message ?: "An internal error occured" ); logToFile(ex) }

fun BukkitPlugin.logToFile(ex: Exception) = logToFile { ex.printStackTrace(this) }
fun BukkitPlugin.logtoFile(msg: String) = logToFile { println(msg) }
val BukkitPlugin.logFile
    get() = dataFolder["log.txt"].apply { if (!exists()) createNewFile() }

fun BukkitPlugin.logToFile(action: PrintWriter.() -> Unit) =
        PrintWriter(FileWriter(logFile, true), true)
                .apply { print(currentDate); action() }.close()

fun BukkitSender.msg(text: TextComponent) = spigot().sendMessage(text)
fun BukkitSender.msg(ex: Exception) { ex.message?.also(::msg) }
fun BukkitSender.msg(msg: String) {
    try{
        msg(textOf(msg))
    } catch (ex: Error) {
        sendMessage(msg.translateColorCode())
    }
}

fun BukkitSender.execute(cmd: String) = Bukkit.dispatchCommand(this, cmd)

val BukkitConfiguration.keys get() = getKeys(false)
fun BukkitConfiguration.section(path: String) = getConfigurationSection(path)
val BukkitConfiguration.sections get() = keys.map { section(it) }
val BukkitConfigurationSection.keys get() = getKeys(false)
fun BukkitConfigurationSection.section(path: String) = getConfigurationSection(path)
val BukkitConfigurationSection.sections get() = keys.map { section(it) }
