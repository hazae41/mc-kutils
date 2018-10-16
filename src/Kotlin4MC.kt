package fr.rhaz.minecraft

import com.google.gson.JsonParser
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatColor.LIGHT_PURPLE
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.event.player.PlayerJoinEvent
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import java.util.function.BiConsumer
import java.util.function.Consumer

// ----------------------------- TYPE ALIASES -----------------------------
typealias BungeePlugin = net.md_5.bungee.api.plugin.Plugin
typealias BungeeSender = net.md_5.bungee.api.CommandSender
typealias BungeeEvent = net.md_5.bungee.api.plugin.Event
typealias BungeeListener = net.md_5.bungee.api.plugin.Listener
typealias BungeeEventHandler = net.md_5.bungee.event.EventHandler
typealias BungeeConfiguration = net.md_5.bungee.config.Configuration
typealias BungeeYaml = net.md_5.bungee.config.YamlConfiguration
typealias BungeeConfigurationProvider = net.md_5.bungee.config.ConfigurationProvider
typealias BungeeCommand = net.md_5.bungee.api.plugin.Command

typealias BukkitPlugin = org.bukkit.plugin.java.JavaPlugin
typealias BukkitSender = org.bukkit.command.CommandSender
typealias BukkitEvent = org.bukkit.event.Event
typealias BukkitListener = org.bukkit.event.Listener
typealias BukkitEventPriority = org.bukkit.event.EventPriority
typealias BukkitEventHandler = org.bukkit.event.EventHandler
typealias BukkitYamlConfiguration = org.bukkit.configuration.file.YamlConfiguration
typealias BukkitCommandExecutor = org.bukkit.command.CommandExecutor

typealias SpongePlugin = org.spongepowered.api.plugin.Plugin

// ----------------------------- LOGGING -----------------------------
fun BukkitPlugin.info(msg: String) = logger.info(msg)
fun BukkitPlugin.warning(msg: String) = logger.warning(msg)
fun BukkitPlugin.severe(msg: String) = logger.severe(msg)

// ----------------------------- KOTLIN4MC PLUGIN -----------------------------

class Kotlin4Bukkit: BukkitPlugin(){
    override fun onEnable() = update(58015, LIGHT_PURPLE)
}
class Kotlin4Bungee: BungeePlugin(){
    override fun onEnable() = update(58015, LIGHT_PURPLE)
}

@SpongePlugin(id = "kotlin4mc", name = "Kotlin4Sponge")
class Kotlin4Sponge

// ----------------------------- JAVA COMPAT -----------------------------
val unit = Unit
val String.lc get() = toLowerCase()
fun <T> listener(callable: Consumer<T>): Function1<T, Unit> = { t -> callable.accept(t); Unit }
fun <T,U> listener(callable: BiConsumer<T, U>): Function2<T, U, Unit> = { t, u -> callable.accept(t, u); Unit }
fun <T,U,V> listener(callable: TriConsumer<T, U, V>): Function3<T, U, V, Unit> = { t, u, v -> callable.accept(t, u, v); Unit }

// ----------------------------- FILE ACCESS -----------------------------
operator fun File.get(key: String) = File(this, key)

// ----------------------------- MESSAGING -----------------------------
fun BungeeSender.msg(msg: String) = msg(text(msg))
fun BungeeSender.msg(text: TextComponent) = sendMessage(text)
fun text(string: String) = TextComponent(string.replace("&", "§"))

// ----------------------------- UPDATES CHECKER -----------------------------
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

// ----------------------------- CONFIG LOADING -----------------------------
val BungeePlugin.provider get() = BungeeConfigurationProvider.getProvider(BungeeYaml::class.java)
fun BungeePlugin.load(
    file: File,
    resource: String = file.nameWithoutExtension+"/bungee.yml"
) = try {
    if (!dataFolder.exists()) dataFolder.mkdir()
    if (!file.exists()) Files.copy(getResourceAsStream(resource), file.toPath())
    provider.load(file)
} catch (e: IOException){ e.printStackTrace(); null }
fun BungeePlugin.save(config: BungeeConfiguration, file: File) = provider.save(config, file)

fun BukkitPlugin.load(
    file: File,
    resource: String = file.nameWithoutExtension+"/bukkit.yml"
): BukkitYamlConfiguration? {
    if (!file.parentFile.exists()) file.parentFile.mkdir()
    if (!file.exists()) Files.copy(getResource(resource), file.toPath())
    return BukkitYamlConfiguration.loadConfiguration(file) ?: null;
}

// ----------------------------- LISTENER -----------------------------
inline fun <reified T: BukkitEvent> BukkitPlugin.listen(
    priority: BukkitEventPriority = BukkitEventPriority.NORMAL,
    crossinline callback: (T) -> Unit
){
    server.pluginManager.registerEvent(
        T::class.java, object: BukkitListener {},
        priority, { _, it -> callback(it as T) },
        this
    )
}

// ----------------------------- COMMANDS -----------------------------
fun BungeePlugin.command(
    name: String,
    permission: String,
    vararg aliases: String,
    callback: (BungeeSender, Array<String>) -> Unit
){
    proxy.pluginManager.registerCommand(this,
        object: Command(name, permission, *aliases){
            override fun execute(sender: CommandSender, args: Array<String>)
                = callback(sender, args)
        }
    )
}

fun BungeePlugin.command(
    name: String,
    callback: (BungeeSender, Array<String>) -> Unit
){
    proxy.pluginManager.registerCommand(this,
        object: BungeeCommand(name){
            override fun execute(sender: BungeeSender, args: Array<String>)
                = callback(sender, args)
        }
    )
}

fun BukkitPlugin.command(
    name: String,
    callback: (BukkitSender, Array<String>) -> Unit
){
    getCommand(name).apply {
        executor = BukkitCommandExecutor {
            sender, _, _, args ->
            true.also{callback(sender, args)}
        }
    }
}

// ----------------------------- SCHEDULER -----------------------------
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
