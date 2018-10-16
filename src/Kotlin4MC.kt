package fr.rhaz.minecraft

import com.google.gson.JsonParser
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatColor.*
import net.md_5.bungee.api.CommandSender as BungeeSender
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ClickEvent.Action.*
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.PostLoginEvent
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import java.io.File
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

// Java compat for the Unit type
// Usage: getUnit() <=> Unit.INSTANCE
val unit = Unit

// Get the lowercase version of any String
// Usage "HeLlO wOrLd".lc <=> *.toLowerCase() => "hello world"
val String.lc get() = toLowerCase()

// Usage: dataFolder["config.yml"] <=> File(dataFolder, "config.yml")
operator fun File.get(key: String) = File(this, key)

// Short messages sending
// Usage: player.msg("hello world") <=> player.sendMessage(TextComponent("hello world".replace("&", "§")))
fun BungeeSender.msg(msg: String) = msg(text(msg))
fun BungeeSender.msg(text: TextComponent) = sendMessage(text)
// Componentize and colorize any message
fun text(string: String) = TextComponent(string.replace("&", "§"))

// Get latest version of a plugin ID
// What's my plugin ID?
// Given this resource: https://www.spigotmc.org/resources/commandsdispatcher-send-commands-to-your-servers-using-sockets.9854/
// The plugin ID is 9854
// Usage: spiget(9854){
//   your code using "it" as the latest version
// }
fun spiget(id: Int, callback: (String) -> Unit) = Thread {
    try {
        val base = "https://api.spiget.org/v2/resources/"
        val conn = URL("$base$id/versions?size=100").openConnection()
        val json = InputStreamReader(conn.inputStream).let { JsonParser().parse(it).asJsonArray }
        callback(json.last().asJsonObject["name"].asString)
    } catch (e: IOException){}
}.start()


// Check if a version is newer than another
// Usage: "1.0" newerThan "0.9.5" => true
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

// Notifications of plugin updates for Bukkit
// Usage: update(9854, RED, "myplugin.update")
// Will check the latest version of plugin ID 9854
// the message will be in red
// and sent to players with permission "myplugin.update"
fun BukkitPlugin.update(id: Int, color: ChatColor = LIGHT_PURPLE, permission: String = "rhaz.update")
    = spiget(id) here@{

        if(!(it newerThan description.version)) return@here;

        val url = "https://www.spigotmc.org/resources/$id"
        val message = text(
            "An update is available for ${description.name}!" +
            " Download it here: $url"
        ).apply {
            this.color = color
            clickEvent = ClickEvent(OPEN_URL, url)
        }

        server.scheduler.runTaskLater(this, {
            server.consoleSender.spigot().sendMessage(message);
        }, 0)

        listen<PlayerJoinEvent>{
            if(it.player.hasPermission(permission))
                it.player.spigot().sendMessage(message)
        }
    }

// Notifications of plugin updates for BungeeCord
// Usage: update(9854, RED, "myplugin.update")
// Will check the latest version of plugin ID 9854
// the message will be in red
// and sent to players with permission "myplugin.update"
fun BungeePlugin.update(id: Int, color: ChatColor = LIGHT_PURPLE, permission: String = "rhaz.update")
    = spiget(id) here@{
        if(!(it newerThan description.version)) return@here;

        val url = "https://www.spigotmc.org/resources/$id"
        val message = text(
            "An update is available for ${description.name}!" +
            " Download it here: $url"
        ).apply {
            this.color = color
            clickEvent = ClickEvent(OPEN_URL, url)
        }

        proxy.scheduler.schedule(this, {
            proxy.console.sendMessage(message);
        }, 0, TimeUnit.SECONDS)

        proxy.pluginManager.registerListener(this, object : BungeeListener {
            @BungeeEventHandler
            fun onJoin(e: PostLoginEvent) {
                if (e.player.hasPermission(permission))
                    e.player.sendMessage(message)
            }
        })
    }

inline fun <reified T: Event> BukkitPlugin.listen(
    priority: EventPriority = EventPriority.NORMAL,
    crossinline callback: (T) -> Unit
){
    server.pluginManager.registerEvent(
        T::class.java, object: BukkitListener {},
        priority, { _, it -> callback(it as T) },
        this
    )
}

fun BukkitPlugin.schedule(
    async: Boolean = false,
    delay: Long? = null,
    period: Long? = null,
    unit: TimeUnit? = null,
    callback: () -> Unit
){
    if(period != null){
        var delay = delay ?: 0
        delay = unit?.toSeconds(delay)?.let{it*20} ?: delay
        val period = unit?.toSeconds(period)?.let{it*20} ?: period

        if(async) server.scheduler.runTaskTimerAsynchronously(this, callback, delay, period)
        else server.scheduler.runTaskTimer(this, callback, delay, period)
        return;
    }
    if(delay != null){
        val delay = unit?.toSeconds(delay)?.let{it*20} ?: delay
        if(async) server.scheduler.runTaskLaterAsynchronously(this, callback, delay)
        else server.scheduler.runTaskLater(this, callback, delay)
        return;
    }
    if(async) server.scheduler.runTaskAsynchronously(this, callback)
    else server.scheduler.runTask(this, callback)
}
