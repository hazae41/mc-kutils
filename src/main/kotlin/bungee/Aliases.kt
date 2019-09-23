package hazae41.minecraft.kutils.bungee

import net.md_5.bungee.api.ProxyServer

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

val proxy get() = ProxyServer.getInstance()