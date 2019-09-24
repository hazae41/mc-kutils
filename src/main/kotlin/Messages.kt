package hazae41.minecraft.kutils

import net.md_5.bungee.api.chat.TextComponent

fun String.translateColorCode() = replace(Regex("&([A-Za-z0-9])")) { "§" + it.groups[1]!!.value }

fun textOf(string: String, builder: TextComponent.() -> Unit = {}) =
    TextComponent(*TextComponent.fromLegacyText(string.translateColorCode())).apply(builder)

class PluginException(msg: String) : Exception("&c$msg")

fun error(msg: String): Nothing = throw PluginException(msg)

fun colored(msg: String?, f: (String) -> Unit) {
    if (!msg.isNullOrBlank()) f(msg.translateColorCode())
}