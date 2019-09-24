package hazae41.minecraft.kutils.bukkit

import org.bukkit.Bukkit

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

fun BukkitSender.execute(cmd: String) = Bukkit.dispatchCommand(this, cmd)

val BukkitConfiguration.keys get() = getKeys(false)
fun BukkitConfiguration.section(path: String) = getConfigurationSection(path)
val BukkitConfiguration.sections get() = keys.map { section(it) }
val BukkitConfigurationSection.keys get() = getKeys(false)
fun BukkitConfigurationSection.section(path: String) = getConfigurationSection(path)
val BukkitConfigurationSection.sections get() = keys.map { section(it) }
