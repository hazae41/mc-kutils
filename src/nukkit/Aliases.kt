@file:JvmName("Kotlin4Nukkit")
@file:JvmMultifileClass

package fr.rhaz.minecraft.kotlin.nukkit

import cn.nukkit.lang.TextContainer
import fr.rhaz.minecraft.kotlin.currentDate
import fr.rhaz.minecraft.kotlin.get
import java.io.FileWriter
import java.io.PrintWriter

typealias NukkitPlugin = cn.nukkit.plugin.PluginBase
typealias NukkitSender = cn.nukkit.command.CommandSender
typealias NukkitEvent = cn.nukkit.event.Event
typealias NukkitEventPriority = cn.nukkit.event.EventPriority
typealias NukkitListener = cn.nukkit.event.Listener
typealias NukkitEventHandler = cn.nukkit.event.EventHandler
typealias NukkitCommand = cn.nukkit.command.Command

fun NukkitPlugin.info(msg: String) = logger.info(msg.replace("&", "§"))
fun NukkitPlugin.info(ex: Exception) {
    ex.message?.also(::info)
}

fun NukkitPlugin.warning(msg: String) = logger.warning(msg.replace("&", "§"))
fun NukkitPlugin.warning(ex: Exception) {
    ex.message?.also(::warning)
}

fun NukkitPlugin.critical(msg: String) = logger.critical(msg.replace("&", "§"))
fun NukkitPlugin.critical(ex: Exception) {
    ex.message?.also(::info)
}

fun NukkitPlugin.emergency(msg: String) = logger.emergency(msg.replace("&", "§"))
fun NukkitPlugin.emergency(ex: Exception) {
    ex.message?.also(::emergency)
}

fun NukkitPlugin.debug(msg: String) = logger.debug(msg.replace("&", "§"))
fun NukkitPlugin.debug(ex: Exception) {
    ex.message?.also(::debug)
}

fun NukkitPlugin.log(ex: Exception) = log { ex.printStackTrace(this) }
fun NukkitPlugin.log(msg: String) = log { println(msg) }
val NukkitPlugin.log
    get() =
        dataFolder["log.txt"].apply { if (!exists()) createNewFile() }

fun NukkitPlugin.log(action: PrintWriter.() -> Unit) =
        PrintWriter(FileWriter(log, true), true)
                .apply { print(currentDate); action() }.close()

fun NukkitSender.msg(msg: String) = sendMessage(msg.replace("&", "§"))
fun NukkitSender.msg(text: TextContainer) = sendMessage(text.apply { this.text = this.text.replace("&", "§") })
fun NukkitSender.msg(ex: Exception) {
    ex.message?.also(::msg)
}

fun NukkitSender.execute(cmd: String) = server.dispatchCommand(this, cmd)