package hazae41.minecraft.kutils

import java.io.File

operator fun File.get(path: String) = File(this, path)
operator fun File.contains(name: String) = name in list().orEmpty()