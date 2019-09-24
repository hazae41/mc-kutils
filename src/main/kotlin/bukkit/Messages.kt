package hazae41.minecraft.kutils.bukkit

import hazae41.minecraft.kutils.colored
import hazae41.minecraft.kutils.currentDate
import hazae41.minecraft.kutils.get
import hazae41.minecraft.kutils.textOf
import net.md_5.bungee.api.chat.TextComponent
import java.io.FileWriter
import java.io.PrintWriter

fun BukkitPlugin.info(msg: String?) = colored(msg, logger::info)
fun BukkitPlugin.info(ex: Exception) = info(ex.message)

fun BukkitPlugin.warning(msg: String?) = colored(msg, logger::warning)
fun BukkitPlugin.warning(ex: Exception) = warning(ex.message)

fun BukkitPlugin.severe(msg: String?) = colored(msg, logger::severe)
fun BukkitPlugin.severe(ex: Exception) = severe(ex.message)

fun BukkitPlugin.error(ex: Exception) {
    severe(ex.message ?: "&cAn internal error occured, check the logs");
    logToFile(ex)
}

fun BukkitPlugin.logToFile(ex: Exception) = logToFile { ex.printStackTrace(this) }
fun BukkitPlugin.logToFile(msg: String) = logToFile { println(msg) }

val BukkitPlugin.logFile
    get() = dataFolder["log.txt"].apply { if (!exists()) createNewFile() }

fun BukkitPlugin.logToFile(action: PrintWriter.() -> Unit) =
    PrintWriter(FileWriter(logFile, true), true)
        .apply { print(currentDate); action() }.close()

fun BukkitSender.msg(msg: String?) {
    try {
        if (msg != null) msg(textOf(msg))
    } catch (ex: Error) {
        colored(msg, ::sendMessage)
    }
}

fun BukkitSender.msg(text: TextComponent) = spigot().sendMessage(text)
fun BukkitSender.msg(ex: Exception) = msg(ex.message)