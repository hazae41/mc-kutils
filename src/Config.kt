@file:JvmName("Kotlin4MC")
@file:JvmMultifileClass

package fr.rhaz.minecraft.kotlin

import com.sun.org.apache.xpath.internal.operations.Bool
import org.bukkit.Color
import org.bukkit.inventory.ItemStack
import java.io.File
import kotlin.reflect.KProperty

open class BukkitConfig(val plugin: BukkitPlugin, var file: File){

    init { reload() }
    lateinit var config: BukkitYamlConfiguration

    fun reload(){
        config = plugin.load(file)
        ?: throw ex("Could not load ${file.name}")
    }

    val sections get() = config.sections

    var autoSave = true
    fun save() = config.save(file)

    inner class any(val path: String, val def: Any? = null){
        operator fun getValue(ref: Any?, prop: KProperty<*>) = config.get(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Any?) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class list(val path: String, val def: List<Any> = emptyList()){
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<*> = config.getList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Any>) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class boolean(val path: String, val def: Boolean = false){
        operator fun getValue(ref: Any?, prop: KProperty<*>) = config.getBoolean(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Boolean) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class booleanList(val path: String){
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Boolean> = config.getBooleanList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Boolean>) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }


    inner class string(val path: String, val def: String = ""){
        operator fun getValue(ref: Any?, prop: KProperty<*>) = config.getString(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: String) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class stringList(val path: String){
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<String> = config.getStringList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<String>) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class int(val path: String, val def: Int = 0){
        operator fun getValue(ref: Any?, prop: KProperty<*>) = config.getInt(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Int) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class intList(val path: String){
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Int> = config.getIntegerList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Int>) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class long(val path: String, val def: Long = 0L){
        operator fun getValue(ref: Any?, prop: KProperty<*>) = config.getLong(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Long) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class longList(val path: String){
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Long> = config.getLongList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Long>) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class double(val path: String, val def: Double = 0.0){
        operator fun getValue(ref: Any?, prop: KProperty<*>) = config.getDouble(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Double) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class doubleList(val path: String){
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Double> = config.getDoubleList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Double>) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class byteList(val path: String){
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Byte> = config.getByteList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Byte>) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class floatList(val path: String){
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Float> = config.getFloatList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Float>) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class charList(val path: String){
        operator fun getValue(ref: Any?, prop: KProperty<*>): List<Char> = config.getCharacterList(path)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: List<Char>) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class color(val path: String, val def: Color? = null){
        operator fun getValue(ref: Any?, prop: KProperty<*>): Color? = config.getColor(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: Color?) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }

    inner class item(val path: String, val def: ItemStack? = null){
        operator fun getValue(ref: Any?, prop: KProperty<*>): ItemStack? = config.getItemStack(path, def)
        operator fun setValue(ref: Any?, prop: KProperty<*>, value: ItemStack?) {
            config.set(path, value); if(autoSave) config.save(file)
        }
    }
}

lateinit var plugin: BukkitPlugin
object MyConf: BukkitConfig(plugin, File("")){
    var debug by boolean("debug")
    var number by int("number")
    val players by stringList("players")
    val item by item("item")
}

fun BukkitPlugin.test(){
    MyConf.debug
}