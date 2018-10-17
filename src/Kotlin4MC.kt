package fr.rhaz.minecraft

import com.google.gson.JsonParser
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatColor.LIGHT_PURPLE
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.event.EventBus
import org.bukkit.event.player.PlayerJoinEvent
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Method
import java.net.URL
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.function.BiConsumer
import java.util.function.Consumer

// ----------------------------- TYPE ALIASES -----------------------------
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

fun BungeePlugin.info(msg: String) = logger.info(msg)
fun BungeePlugin.warning(msg: String) = logger.warning(msg)
fun BungeePlugin.severe(msg: String) = logger.severe(msg)

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
fun <T> listener(callable: Consumer<T>): Function1<T, Unit> = { t -> callable.accept(t); Unit }
fun <T,U> listener(callable: BiConsumer<T, U>): Function2<T, U, Unit> = { t, u -> callable.accept(t, u); Unit }
fun <T,U,V> listener(callable: TriConsumer<T, U, V>): Function3<T, U, V, Unit> = { t, u, v -> callable.accept(t, u, v); Unit }

// ----------------------------- OTHERS -----------------------------
operator fun File.get(key: String) = File(this, key)
val String.lc get() = toLowerCase()

// ----------------------------- MESSAGING -----------------------------
fun BungeeSender.msg(msg: String) = msg(text(msg))
fun BungeeSender.msg(text: TextComponent) = sendMessage(text)
fun text(string: String) = TextComponent(string.replace("&", "§"))
fun BukkitSender.msg(text: TextComponent) = spigot().sendMessage(text)
fun BukkitSender.msg(msg: String) = msg(text(msg))

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

        schedule{
            server.consoleSender.msg(message)
        }

        listen<PlayerJoinEvent>{
            if(it.player.hasPermission(permission))
                it.player.msg(message)
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

        schedule{
            proxy.console.msg(message);
        }

        listen<PostLoginEvent>{
            if(it.player.hasPermission(permission))
                it.player.msg(message)
        }
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
    return BukkitYamlConfiguration.loadConfiguration(file);
}

// ----------------------------- LISTENERS -----------------------------
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

inline fun <reified T: BungeeEvent> BungeePlugin.listen(
    priority: Byte = BungeeEventPriority.NORMAL,
    crossinline callback: (T) -> Unit
){
    val pm = proxy.pluginManager
    val pmc = pm::class.java
    val bus = pmc.getDeclaredField("eventBus").run {
        isAccessible = true; get(pm) as EventBus
    }
    val busc = bus::class.java
    val lock = busc.getDeclaredField("lock").run {
        isAccessible = true; get(bus) as Lock
    }
    val bLaP = busc.getDeclaredField("byListenerAndPriority").run {
        isAccessible = true; get(bus) as HashMap<Class<*>, Map<Byte, Map<Object, Array<Method>>>>
    }
    val priorities = bLaP[T::class.java] as? HashMap<Byte, Map<Object, Array<Method>>>
            ?: HashMap<Byte, Map<Object, Array<Method>>>()
                    .also { bLaP[T::class.java] = it }
    val handlers = priorities[priority] as? HashMap<Object, Array<Method>>
            ?: HashMap<Object, Array<Method>>()
                    .also { priorities[priority] = it }
    val listener = object: BungeeListener{
        fun onEvent(it: T) = callback(it)
    }
    handlers[listener as Object] = arrayOf(listener::class.java.getMethod("onEvent", BungeeEvent::class.java))
    busc.getDeclaredMethod("bakeHandlers", Class::class.java).apply {
        isAccessible = true; invoke(bus, T::class.java)
    }
    proxy.pluginManager.registerListener(this, listener)
}

// ----------------------------- COMMANDS -----------------------------
fun BungeePlugin.command(
    name: String,
    permission: String,
    vararg aliases: String,
    callback: (BungeeSender, Array<String>) -> Unit
){
    proxy.pluginManager.registerCommand(this,
        object: BungeeCommand(name, permission, *aliases){
            override fun execute(sender: BungeeSender, args: Array<String>)
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

fun BungeePlugin.schedule(
    async: Boolean = false,
    delay: Long? = null,
    period: Long? = null,
    unit: TimeUnit? = null,
    callback: () -> Unit
){
    if(period != null){
        var delay = delay ?: 0
        val unit = unit ?: TimeUnit.MILLISECONDS.also{ delay *= 50 }
        if(async)
            proxy.scheduler.schedule(this, {
                proxy.scheduler.runAsync(this, callback)
            }, delay, period, unit)
        else proxy.scheduler.schedule(this, callback, delay, period, unit)
        return;
    }
    if(delay != null){
        var delay = delay
        val unit = unit ?: TimeUnit.MILLISECONDS.also{ delay *= 50 }
        if(async)
            proxy.scheduler.schedule(this, {
                proxy.scheduler.runAsync(this, callback)
            }, delay, unit)
        else proxy.scheduler.schedule(this, callback, delay, unit)
        return;
    }
    if(async) proxy.scheduler.runAsync(this, callback)
    else proxy.scheduler.schedule(this, callback, 0, TimeUnit.MILLISECONDS)
}
