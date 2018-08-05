package fr.rhaz.minecraft

import net.md_5.bungee.api.plugin.Plugin as BungeePlugin
import org.bukkit.plugin.java.JavaPlugin as BukkitPlugin
import org.spongepowered.api.plugin.Plugin as SpongePlugin

class Kotlin4Bukkit: BukkitPlugin()
class Kotlin4Bungee: BungeePlugin()

@SpongePlugin(id = "kotlin4mc", name = "Kotlin4Sponge")
class Kotlin4Sponge