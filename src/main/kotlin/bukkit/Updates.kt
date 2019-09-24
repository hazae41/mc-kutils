package hazae41.minecraft.kutils.bukkit

import hazae41.minecraft.kutils.catch
import hazae41.minecraft.kutils.isNewerThan
import hazae41.minecraft.kutils.spiget
import hazae41.minecraft.kutils.textOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import org.bukkit.event.player.PlayerJoinEvent

fun BukkitPlugin.update(
    id: Int,
    color: ChatColor = ChatColor.LIGHT_PURPLE,
    permission: String = "rhaz.update"
) = catch<Exception>(::warning) {
    GlobalScope.launch {
        val new = spiget(id)
            ?: throw Exception("Could not retrieve latest version")

        val old = description.version
        if (!(new isNewerThan old)) return@launch

        val url = "https://www.spigotmc.org/resources/$id"
        val msg = "${color}An update is available for ${description.name} ($old -> $new): $url"

        schedule { server.consoleSender.msg(msg) }
        listen<PlayerJoinEvent> {
            if (it.player.hasPermission(permission))
                try {
                    val text = textOf(msg) { clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, url) }
                    it.player.msg(text)
                } catch (ex: Error) {
                    it.player.msg(msg)
                }
        }
    }
}