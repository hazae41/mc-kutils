@file:JvmName("Kotlin4Bukkit")
@file:JvmMultifileClass

package fr.rhaz.minecraft.kotlin.bukkit

import fr.rhaz.minecraft.kotlin.date
import fr.rhaz.minecraft.kotlin.get
import fr.rhaz.minecraft.kotlin.text
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
typealias BukkitYamlConfiguration = org.bukkit.configuration.file.YamlConfiguration
typealias BukkitFileConfiguration = org.bukkit.configuration.file.FileConfiguration
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

fun BukkitPlugin.log(ex: Exception) = log { ex.printStackTrace(this) }
fun BukkitPlugin.log(msg: String) = log { println(msg) }
val BukkitPlugin.log
    get() =
        dataFolder["log.txt"].apply { if (!exists()) createNewFile() }

fun BukkitPlugin.log(action: PrintWriter.() -> Unit) =
        PrintWriter(FileWriter(log, true), true)
                .apply { print(date); action() }.close()

fun BukkitSender.msg(text: TextComponent) = spigot().sendMessage(text)
fun BukkitSender.msg(msg: String) = msg(text(msg))
fun BukkitSender.msg(ex: Exception) {
    ex.message?.also(::msg)
}

fun BukkitSender.execute(cmd: String) = Bukkit.dispatchCommand(this, cmd)

val BukkitYamlConfiguration.keys get() = getKeys(false)
fun BukkitYamlConfiguration.section(path: String) = getConfigurationSection(path)
val BukkitYamlConfiguration.sections get() = keys.map { section(it) }
val BukkitConfigurationSection.keys get() = getKeys(false)
fun BukkitConfigurationSection.section(path: String) = getConfigurationSection(path)
val BukkitConfigurationSection.sections get() = keys.map { section(it) }
