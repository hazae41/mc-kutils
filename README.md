<h3 align=center>
  <img src="https://i.imgur.com/JHmrNWZ.png"/><br>
  :potable_water: Spigot & BungeeCord :wavy_dash:
</h3>
<br><hr><br>

It includes Kotlin stdlib and a lot of tools for Minecraft plugins development.

[Go to Spigot](https://www.spigotmc.org/resources/kotlin4mc-let-kotlin-improve-your-plugins.58015/)

### Simplified componentization

You can componentize and colorize any message

##### With Kotlin4MC

```kotlin
textOf("&bHello world!")
```

##### Without Kotlin4MC

```kotlin
TextComponent(*fromLegacyText(ChatColor.translateAlternateColorCodes("&", "&bHello world")))
```

### Simplified messages sending

Let's send a message to a player (any Player, ProxiedPlayer, CommandSender is supported)

##### With Kotlin4MC

```kotlin
player.msg("&bHello world!")
```

##### Without Kotlin4MC (not deprecated)

```kotlin
player.sendMessage(TextComponent(*fromLegacyText(ChatColor.translateAlternateColorCodes("&", "&bHello world"))))
```

### Simplified event listening

You can use listen() to easily listen to events

##### With Kotlin4MC

```kotlin
listen<PlayerJoinEvent>{
  it.player.msg("Hello world!")
}
```

##### Without Kotlin4MC

```kotlin
server.pluginManager.registerEvent(object: Listener{
  @EventHandler
  fun onPlayerJoin(e: PlayerJoinEvent){
      e.player.sendMessage("Hello world!")
  }
}, this)
```

##### In Java

```java
getServer().getPluginManager().registerEvents(new Listener() {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e){
      e.getPlayer().sendMessage("Hello world!");
  }
}, this);
```

You can also use priorities

```kotlin
listen<PlayerJoinEvent>(HIGHEST){
  it.player.msg("This will be the first")
}
```

### Simplified scheduling

```kotlin
schedule(delay = 10){
    // this will be executed after 10 ticks
}

schedule(delay = 10, period = 20){
    // this will be executed after 10 ticks then each 20 ticks (1 second)
}

schedule(async = true){
    // this will be executed asynchronously now
}

schedule(true, delay = 20){
    // this will be executed asynchronously after 20 ticks (1 second)
}

schedule(true, period = 3, unit = TimeUnit.MINUTES){
    // this will be executed asynchronously each 3 minutes
}

// Tasks can cancel themselves
schedule(period = ...) {
    if(...) cancel()
}

```

Warning: the default unit is ticks, even on BungeeCord

Warning 2: never use TimeUnit.NANOSECONDS and TimeUnit.MICROSECONDS

Warning 3: do not use TimeUnit.MILLISECONDS with Bukkit

### Fast access to files of a directory

You can easily access a file or a subfolder with the get() operator applied to a File

##### With Kotlin4MC
```kotlin
val config = dataFolder["config.yml"]
val lang = dataFolder["langs"]["en_US.yml"]
```

##### Without Kotlin4MC
```kotlin
val config = File(dataFolder, "config.yml")
val lang = File(File(dataFolder, "langs"), "en_US.yml")
```

### Type aliases for multi-platform plugins

If you want to specify the platform of any type, just add "Bungee" or "Bukkit" before its name

##### With Kotlin4MC: both platforms on the same file
```kotlin
class MyPluginOnBungee: BungeePlugin(){
    override fun onEnable() = info("Hello Bungee!")
}

class MyPluginOnBukkit: BukkitPlugin(){
    override fun onEnable() = info("Hello Bukkit!")
}
```

##### Without Kotlin4MC: two separated files for each platform

### Simple lower case

You can use .lc after any String to get its lower case version

##### With Kotlin4MC
```kotlin
"HeLLo WorLd".lowerCase
```

##### Without Kotlin4MC
```kotlin
"HeLLo WorLd".toLowerCase() 
```

### Simple configuration loading

Load the config from the data folder, otherwise, copy it from the resource "config.yml".

If there is an error, do not continue.

```kotlin
val configFile = dataFolder["config.yml"]
val config = loadConfig(configFile, "configs/config.yml")
// Will load resource configs/config.yml
```

You can ommit the resource argument, it will copy the resource of the same name as the file

```kotlin
val configFile = dataFolder["config.yml"]
val config = loadConfig(configFile) // Will load resource "config.yml"
```

### Delegated configuration

You can use delegated configuration to manage big configurations
```kotlin
class MyPlugin: BukkitPlugin(){

    object MyConfig: ConfigFile("config"){
        var debug by boolean("debug")
        var alertMessage by string("alert-message")
        var enabledWorlds by stringList("enabled-worlds")
    }

    override fun onEnable(){
        // Initialize the object with the current file
        init(MyConfig) // (it will copy the resource config.yml to the data folder)
        // We can access properties dynamically
        info("debug: " + MyConfig.debug)
        // Change them (if "var" is used)
        MyConfig.debug = false
        // Save them (if auto-saving is disabled)
        MyConfig.save()
        // Reload the config from the file
        MyConfig.reload()
    }
}
```

You have to init the configuration only if you're using a resource
````kotlin
ConfigFile(file: File)
// This will load the config from the file
// No init() required

ConfigFile(path: String)
// This will copy the resource located at path to a file of the same path in the data folder
// Needs to be initialized with init(plugin) or plugin.init(config)
// You can ommit the .yml in the path
// You can specify the resource to copy with init(plugin, resourcePath)
````

You can pass multiple ConfigFile to the init() like
````kotlin
init(Options, Players, Worlds, ...)
````

By default, the configuration is saved every time a property is changed

You can disable auto-saving by doing
````kotlin
MyConfig.autoSave = false
````
or changing the constructor
````kotlin
object MyConfig: ConfigFile("config", false){
    ...
}
````

You can also use sections
````kotlin
object MySection: ConfigSection(MyConfig, "mysection"){
    val host by string("host")
    val port by int("port")
}
````

It is recommended to use objects instead of class, but you can use them if you need parameters
````kotlin
inner class PlayerInfo(uuid: UUID): ConfigFile(dataFolder["$uuid.yml"]){
    val friends by stringList("friends")
    // ...
}

val Player.info get() = PlayerInfo(uniqueId)
val Player.friends get() = info.friends
````

````kotlin
class SocketConfig(id: String): ConfigSection(MyConfig, "sockets.$id"){
    val host by string("host")
    val port by int("port")
}

fun address(id: String){
    val config = SocketConfig(id)
    return config.host + ":" + config.port
}

````

### Fast logging

You can use info(), warning() and severe() with String or Exception to log them in the console

You can also use logToFile() to write to a file named "log.txt" in your plugin's data folder

##### With Kotlin4MC
```kotlin
info("Hello world!")
```

##### Without Kotlin4MC
```kotlin
logger.info("Hello world!")
```

##### In Java
```java
getLogger().info("Hello world!");
```

### Simplified commands

##### With Kotlin4MC
```kotlin
// Example on Bukkit
command("hello"){ sender, args ->
    if(args.isEmpty())
        sender.msg("&cWrong arguments, usage: $usage")
    else sender.msg("&bHello!")
}

// You can apply the executor directly to the sender
command("test") { 
    args -> msg("&bYou said $args") 
}
```

##### Without Kotlin4MC
```kotlin
// Bungee
proxy.pluginManager.registerCommand(this,
    object: Command("hello"){
        override fun execute(sender: CommandSender, args: Array<String>){
            sender.sendMessage("§bHello!")
        }
    }
)

// Bukkit
getCommand("hello").executor = CommandExecutor {
    sender, command, label, args ->
    sender.sendMessage("§bHello!")
    true
}
```

### Plugin updates checker

You can check for updates of your plugin using Spiget
![](https://ipfs.io/ipfs/QmZU6234EqPiQVH8Wj6ofkNgsA4UuUTovaMVbp85DPuubF/Capture.PNG)

Just use update() with your Spigot resource ID
```kotlin
update(15938)
```

You can specify the color
```kotlin
update(15938, LIGHT_PURPLE)
```

and the permission (the default permission is "rhaz.update")
```kotlin
update(15938, LIGHT_PURPLE, "myplugin.updates")
```

### Advanced exception catching

Exceptions can be catched with a beautiful syntax

##### With Kotlin4MC
```kotlin
// This will catch any exception and log it as a warning
catch<Exception>(::warning){
    // ex() is a short replacement of Exception()
    throw ex("An error occured")
}
```

##### Without Kotlin4MC
```kotlin
try{
    throw Exception("An error occured")
} catch(ex: Exception){
    warning(ex)
}
```

##### Catch only specific exceptions
```kotlin
// The callback can be ommited, the default one is ::printStackTrace
catch<CommandException>{
    throw Exception("This won't be catched")
}
```

##### Catch and redirect to the sender
```kotlin
val sender: CommandSender = ...
catch<Exception>(sender::msg){
    if(sender !is Player)
        throw ex("&cYou're not a player!")
    sender.gamemode = GameMode.CREATIVE
    sender.msg("&bYou're now in creative mode :)")
}
```

##### Custom callbacks
```kotlin
// Tell the admins about the exception
val admins = server.onlinePlayers.filter{it.hasPermission("test.admin")}
fun tellToAdmins(ex: Exception) = admins.forEach{it.msg(ex)}

catch<Exception>(::tellToAdmins){
    throw ex("Alert!")
}


// Anonymous callback that prepend the warning with "An error occured"
catch<Exception>({ warning("An error occured: ${it.message}")}){
    throw ex(...)
}
```

##### Custom exceptions
```kotlin
class RedException(message: String): Exception(){
    override val message = "&c$message"
}

fun test(){
    catch<RedException>(::warning){
        throw RedException("This message will be red")
    }    
}
```

##### Catching with result, callback and default value
```kotlin
fun default(ex: Exception) =
    {warning(ex); "This is the default message"}()

val msg = catch(::default){
    val line1 = read() ?: throw ex("Could not read first line")
    val line2 = read() ?: throw ex("Could not read second line")
    val line3 = read() ?: throw ex("Could not read third line")
    info("Sucessfully read three lines")
    "$line1, $line2, $line3"
    // Return the three lines separated by "," 
}

// Will print "This is the default message"
// if one of the three lines could not be read
info("The message is: $msg")
```

### Short equality checks

You can use .not() and .eq() to check inequality/equality of any object

"object.not(other)":
- returns null if object == other
- returns object if object != other

"object.eq(other)":
- returns null if object != other
- returns object if object == other

##### With Kotlin4MC
```kotlin
val delay = config.getLong("delay").not(0) // Assignment + Check
    ?: return warning("Delay should not be 0")
```

##### Without Kotlin4MC
```kotlin
val delay = config.getLong("delay")  // Assignment
if(delay == 0) return warning("Delay should not be 0") // Check
```

### How to implement it?

- Gradle: add this to your build.gradle

      repositories {
          maven { url 'https://mymavenrepo.com/repo/NIp3fBk55f5oF6VI1Wso/' }
      }

      dependencies {
          compileOnly 'fr.rhaz.minecraft:kotlin4mc:2.1'
      }


- Maven: add this to your pom.xml

      <repositories>
        <repository>
            <id>rhazdev</id>
            <url>https://mymavenrepo.com/repo/NIp3fBk55f5oF6VI1Wso/</url>
        </repository>
      </repositories>

      <dependencies>
        <dependency>
            <groupId>fr.rhaz.minecraft</groupId>
            <artifactId>kotlin4mc</artifactId>
            <version>2.1</version>
            <scope>provided</scope>
        </dependency>
      </dependencies>
