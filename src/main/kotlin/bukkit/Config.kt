package hazae41.minecraft.kutils.bukkit

import hazae41.minecraft.kutils.get
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.io.File
import kotlin.reflect.KProperty

val BukkitConfiguration.keys get() = getKeys(false)
fun BukkitConfiguration.section(path: String) = getConfigurationSection(path)
val BukkitConfiguration.sections get() = keys.map { section(it) }

val BukkitConfigurationSection.keys get() = getKeys(false)
fun BukkitConfigurationSection.section(path: String) = getConfigurationSection(path)
val BukkitConfigurationSection.sections get() = keys.map { section(it) }

fun BukkitPlugin.saveResource(resource: String, file: File) {
    if (file.exists()) return
    file.parentFile.mkdirs()
    file.createNewFile()
    getResource(resource)!!.copyTo(file.outputStream())
}

fun BukkitPlugin.init(vararg configs: PluginConfigFile) {
    configs.forEach {
        if (it.file != null) throw Exception("Config is already initialized")
        val fileName = "${it.path}.yml"
        val file = dataFolder[fileName]
        saveResource(fileName, file)
        it.file = file
    }
}

abstract class Config {

    abstract var config: ConfigurationSection

    open var autoSave = true

    open val sections get() = config.sections

    open operator fun contains(key: String) = config.contains(key)

    open operator fun get(key: String) = config[key]

    open operator fun set(key: String, value: Any?) {
        val config = config
        config.set(key, value)
        if (autoSave) this.config = config
    }

    open inner class any(val path: String, val def: Any? = null) {
        operator fun getValue(ref: Any?, prop: KProperty<*>) = config.get(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Any?) = set(path, value)
    }

    open inner class list(val path: String, val def: List<Any> = emptyList()) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<*> = config.getList(path).orEmpty()
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Any>) = set(path, value)
    }

    open inner class boolean(val path: String, val def: Boolean = false) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Boolean = config.getBoolean(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Boolean) = set(path, value)
    }

    open inner class booleanList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Boolean> = config.getBooleanList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Boolean>) = set(path, value)
    }

    open inner class string(val path: String, val def: String = "") {
        operator fun getValue(ref: Any?, prop: KProperty<*>): String = config.getString(path, def)!!
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: String) = set(path, value)
    }

    open inner class stringList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<String> = config.getStringList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<String>) = set(path, value)
    }

    open inner class int(val path: String, val def: Int = 0) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Int = config.getInt(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Int) = set(path, value)
    }

    open inner class intList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Int> = config.getIntegerList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Int>) = set(path, value)
    }

    open inner class long(val path: String, val def: Long = 0L) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Long = config.getLong(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Long) = set(path, value)
    }

    open inner class longList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Long> = config.getLongList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Long>) = set(path, value)
    }

    open inner class double(val path: String, val def: Double = 0.0) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Double = config.getDouble(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Double) = set(path, value)
    }

    open inner class doubleList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Double> = config.getDoubleList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Double>) = set(path, value)
    }

    open inner class byteList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Byte> = config.getByteList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Byte>) = set(path, value)
    }

    open inner class floatList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Float> = config.getFloatList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Float>) = set(path, value)
    }

    open inner class shortList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Short> = config.getShortList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Short>) = set(path, value)
    }

    open inner class charList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Char> = config.getCharacterList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Char>) = set(path, value)
    }

    open inner class color(val path: String, val def: Color? = null) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Color? = config.getColor(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Color?) = set(path, value)
    }

    open inner class item(val path: String, val def: ItemStack? = null) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): ItemStack? = config.getItemStack(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: ItemStack?) = set(path, value)
    }

    open inner class offlinePlayer(val path: String, val def: OfflinePlayer? = null) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): OfflinePlayer? = config.getOfflinePlayer(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: OfflinePlayer?) = set(path, value)
    }

    open inner class vector(val path: String, val def: Vector? = null) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Vector? = config.getVector(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Vector?) = set(path, value)
    }

    open inner class location(val path: String, val def: Location? = null) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Location? = config.getLocation(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Location?) = set(path, value)
    }

    open inner class serializable<T : ConfigurationSerializable>(
            val path: String,
            val clazz: Class<T>,
            val def: T? = null
    ) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): T? = config.getSerializable(path, clazz, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: T?) = set(path, value)
    }

    open inner class mapList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Map<*, *>> = config.getMapList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Map<*, *>>) = set(path, value)
    }

    open inner class section(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): ConfigurationSection? = config.getConfigurationSection(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: ConfigurationSection?) = set(path, value)
    }
}

open class ConfigFile(open var file: File?) : Config() {
    open var minDelay = 1000L

    private var _config: ConfigurationSection? = null
    private var lastLoad = 0L

    override var config: ConfigurationSection
        get() {
            val file = file ?: throw Exception("Config is not initialized")
            val currentMillis = System.currentTimeMillis()
            val delay = currentMillis - lastLoad
            return _config?.takeUnless { delay > minDelay }
                    ?: BukkitConfiguration.loadConfiguration(file)?.also { _config = it; lastLoad = currentMillis }
                    ?: throw Exception("Could not load ${file.name}")
        }
        set(value) {
            val file = file ?: throw Exception("Config is not initialized")
            val config = value as? FileConfiguration
                    ?: throw Exception("Could not save ${config.name} to ${file.name}")
            config.save(file)
        }
}

open class PluginConfigFile(open var path: String) : ConfigFile(null)

open class ConfigSection(
        open var parent: Config, open var path: String
) : Config() {

    override var config: ConfigurationSection
        get() {
            val config = parent.config.getConfigurationSection(path)
            return config ?: throw Exception("Could not load $path from ${parent.config.name}")
        }
        set(value) {
            parent[path] = value
        }
}
