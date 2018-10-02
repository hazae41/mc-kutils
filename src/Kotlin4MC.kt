package fr.rhaz.minecraft

import com.google.gson.JsonParser
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatColor.*
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.PostLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.TimeUnit
import net.md_5.bungee.api.plugin.Event as BungeeEvent
import net.md_5.bungee.api.plugin.Listener as BungeeListener
import net.md_5.bungee.api.plugin.Plugin as BungeePlugin
import net.md_5.bungee.config.YamlConfiguration as BungeeYaml
import net.md_5.bungee.event.EventHandler as BungeeEventHandler
import org.bukkit.event.EventHandler as BukkitEventHandler
import org.bukkit.event.Listener as BukkitListener
import org.bukkit.plugin.java.JavaPlugin as BukkitPlugin
import org.spongepowered.api.plugin.Plugin as SpongePlugin

class Kotlin4Bukkit: BukkitPlugin(){
    override fun onEnable() = update(58015, LIGHT_PURPLE)
}
class Kotlin4Bungee: BungeePlugin(){
    override fun onEnable() = update(58015, LIGHT_PURPLE)
}

@SpongePlugin(id = "kotlin4mc", name = "Kotlin4Sponge")
class Kotlin4Sponge

fun CommandSender.msg(msg: String) = msg(text(msg))
fun CommandSender.msg(text: TextComponent) = sendMessage(text)
fun text(string: String) = TextComponent(string.replace("&", "§"))

fun spiget(id: Int, callback: (String) -> Unit) = Thread {
    try {
        val base = "https://api.spiget.org/v2/resources/"
        val conn = URL("$base$id/versions?size=100").openConnection()
        val json = InputStreamReader(conn.inputStream).let { JsonParser().parse(it).asJsonArray }
        callback(json.last().asJsonObject["name"].asString)
    } catch (e: IOException){}
}.start()

infix fun String.newerThan(v: String): Boolean = false.also{
    val s1 = split('.');
    val s2 = v.split('.');
    for(i in 0..Math.max(s1.size,s2.size)){
        if(i !in s1.indices) return false;
        if(i !in s2.indices) return true;
        if(s1[i].toInt() > s2[i].toInt()) return true;
        if(s1[i].toInt() < s2[i].toInt()) return false;
    }
}

fun BukkitPlugin.update(id: Int, color: ChatColor) = spiget(id) here@{

    if(!(it newerThan description.version)) return@here;

    val message = text("An update is available for ${description.name}!").apply {
        val url = "https://www.spigotmc.org/resources/$id"
        text += " Download it here: $url"
        this.color = color
        clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url)
    }

    server.scheduler.runTaskLater(this, {
        server.consoleSender.spigot().sendMessage(message);
    }, 0)

    server.pluginManager.registerEvents(object: BukkitListener {
        @BukkitEventHandler
        fun onJoin(e: PlayerJoinEvent) {
            if(e.player.hasPermission("rhaz.update"))
                e.player.spigot().sendMessage(message)
        }
    }, this)
}

fun BungeePlugin.update(id: Int, color: ChatColor) = spiget(id) here@{

    if(!(it newerThan description.version)) return@here;

    val message = text("An update is available for ${description.name}!").apply {
        val url = "https://www.spigotmc.org/resources/$id"
        text += " Download it here: $url"
        this.color = color
        clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url)
    }

    proxy.scheduler.schedule(this, {
        proxy.console.sendMessage(message);
    }, 0, TimeUnit.SECONDS)

    proxy.pluginManager.registerListener(this, object : BungeeListener {
        @BungeeEventHandler
        fun onJoin(e: PostLoginEvent) {
            if (e.player.hasPermission("rhaz.update"))
                e.player.sendMessage(message)
        }
    })
}