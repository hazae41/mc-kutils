package hazae41.minecraft.kutils.bungee

import hazae41.minecraft.kutils.get
import net.md_5.bungee.config.Configuration
import java.io.File
import kotlin.reflect.KProperty

val configProvider get() = BungeeConfigurationProvider.getProvider(BungeeYaml::class.java)!!

fun BungeePlugin.saveResource(resource: String, file: File) {
    if (file.exists()) return
    file.parentFile.mkdirs()
    file.createNewFile()
    getResourceAsStream(resource).copyTo(file.outputStream())
}

fun BungeeConfiguration.section(path: String) = getSection(path)
val BungeeConfiguration.sections get() = keys.map { section(it) }

fun BungeePlugin.init(vararg configs: PluginConfigFile) {
    configs.forEach {
        if (it.file != null) throw Exception("Config is already initialized")
        val fileName = "${it.path}.yml"
        val file = dataFolder[fileName]
        saveResource(fileName, file)
        it.file = file
    }
}

abstract class Config {

    abstract var config: Configuration

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
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<*> = config.getList(path)
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
        operator fun getValue(ref: Any?, prop: KProperty<*>): String = config.getString(path, def)
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
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Int> = config.getIntList(path)
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

    open inner class byte(val path: String, val def: Byte = 0) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Byte = config.getByte(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Byte) = set(path, value)
    }

    open inner class byteList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Byte> = config.getByteList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Byte>) = set(path, value)
    }

    open inner class float(val path: String, val def: Float = 0f) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Float = config.getFloat(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Float) = set(path, value)
    }

    open inner class floatList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Float> = config.getFloatList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Float>) = set(path, value)
    }

    open inner class short(val path: String, val def: Short = 0) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Short = config.getShort(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Short) = set(path, value)
    }

    open inner class shortList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Short> = config.getShortList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Short>) = set(path, value)
    }

    open inner class char(val path: String, val def: Char = '\u0000') {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Char = config.getChar(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Char) = set(path, value)
    }

    open inner class charList(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Char> = config.getCharList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Char>) = set(path, value)
    }

    open inner class section(val path: String) {
        operator fun getValue(ref: Any?, prop: KProperty<*>): Configuration? = config.getSection(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Configuration?) = set(path, value)
    }
}

open class ConfigFile(open var file: File?) : Config() {
    open var minDelay = 1000L

    private var _config: Configuration? = null
    private var lastLoad = 0L

    override var config: Configuration
        get() {
            val file = file ?: throw Exception("Config is not initialized")
            val currentMillis = System.currentTimeMillis()
            val delay = currentMillis - lastLoad
            return _config?.takeUnless { delay > minDelay }
                ?: configProvider.load(file)?.also { _config = it; lastLoad = currentMillis }
                ?: throw Exception("Could not load ${file.name}")
        }
        set(value) {
            val file = file ?: throw Exception("Config is not initialized")
            configProvider.save(value, file)
        }
}

open class PluginConfigFile(open var path: String) : ConfigFile(null)

open class ConfigSection(
    open var parent: Config, open var path: String
) : Config() {

    override var config: Configuration
        get() {
            val config = parent.config.getSection(path)
            return config ?: throw Exception("Could not load $path from ${parent.javaClass.name}")
        }
        set(value) {
            parent[path] = value
        }
}