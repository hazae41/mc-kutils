package hazae41.minecraft.kutils.bungee

import hazae41.minecraft.kutils.catch
import hazae41.minecraft.kutils.isNewerThan
import hazae41.minecraft.kutils.spiget
import hazae41.minecraft.kutils.textOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatColor.LIGHT_PURPLE
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.event.PostLoginEvent

fun BungeePlugin.update(
    id: Int,
    color: ChatColor = LIGHT_PURPLE,
    permission: String = "kutils.update"
) = catch<Exception>(::warning) {
    GlobalScope.launch {
        val new = spiget(id)
            ?: throw Exception("Could not retrieve latest version")

        val old = description.version

        if (!(new isNewerThan old)) return@launch

        val url = "https://www.spigotmc.org/resources/$id"
        val message = textOf(
            "An update is available for ${description.name} ($old -> $new): $url"
        ).apply {
            this.color = color
            clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url)
        }

        schedule { proxy.console.msg(message) }

        listen<PostLoginEvent> {
            if (it.player.hasPermission(permission))
                it.player.msg(message)
        }
    }
}



