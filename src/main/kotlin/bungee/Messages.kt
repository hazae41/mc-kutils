package hazae41.minecraft.kutils.bungee

import hazae41.minecraft.kutils.colored
import hazae41.minecraft.kutils.currentDate
import hazae41.minecraft.kutils.get
import hazae41.minecraft.kutils.textOf
import net.md_5.bungee.api.chat.TextComponent
import java.io.FileWriter
import java.io.PrintWriter

fun BungeePlugin.info(msg: String?) = colored(msg, logger::info)
fun BungeePlugin.info(msg: Throwable) = info(msg.message)

fun BungeePlugin.warning(msg: String?) = colored(msg, logger::warning)
fun BungeePlugin.warning(msg: Throwable) = info(msg.message)

fun BungeePlugin.severe(msg: String?) = colored(msg, logger::severe)
fun BungeePlugin.severe(msg: Throwable) = info(msg.message)

fun BungeePlugin.error(ex: Exception) {
    severe(ex.message ?: "&cAn internal error occured, check the logs");
    logToFile(ex)
}

fun BungeePlugin.logToFile(ex: Exception) = logToFile { ex.printStackTrace(this) }
fun BungeePlugin.logToFile(msg: String) = logToFile { println(msg) }

val BungeePlugin.logFile
    get() = dataFolder["log.txt"].apply { if (!exists()) createNewFile() }

fun BungeePlugin.logToFile(action: PrintWriter.() -> Unit) =
    PrintWriter(FileWriter(logFile, true), true)
        .apply { print(currentDate); action() }.close()

fun BungeeSender.msg(text: TextComponent) = sendMessage(text)
fun BungeeSender.msg(msg: String?) = colored(msg) { msg(textOf(it)) }
fun BungeeSender.msg(ex: Exception) = msg(ex.message)