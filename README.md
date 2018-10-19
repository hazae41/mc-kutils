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
text("Hello world!")
```

##### Without Kotlin4MC

```kotlin
TextComponent("Hello world".replace("&", "§"))
```

### Simplified messages sending

Let's send a message to a player (any Player, ProxiedPlayer, CommandSender is supported)

##### With Kotlin4MC

```kotlin
player.msg("Hello world!")
```

##### Without Kotlin4MC (deprecated)

```kotlin
player.sendMessage("Hello world!".replace("&", "§"))
```

##### Without Kotlin4MC (not deprecated)

```kotlin
player.sendMessage(TextComponent("Hello world!".replace("&", "§")))
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
class MyPluginOnBungee: BungeePlugin{
    override fun onEnable() = info("Hello Bungee!")
}

class MyPluginOnBukkit: BukkitPlugin{
    override fun onEnable() = info("Hello Bukkit!")
}
```

##### Without Kotlin4MC: two separated files for each platform

### Simple lower case

You can use .lc after any String to get its lower case version

##### With Kotlin4MC
```kotlin
"HeLLo WorLd".lc // Beautiful
```

##### Without Kotlin4MC
```kotlin
"HeLLo WorLd".toLowerCase() // Ugly
```

### Java compat optimization

Java developers that use your Kotlinized plugins can easily access the Unit type by using getUnit() in any context
and they can access lambda functions of Kotlin with listener()

Let's say you have this method in your API

```kotlin
fun performTask(callback: (String) -> Unit) =
    Thread {
        // long task
        val result: String = ...
        callback(result)
    }
```

And Java developers need to use it

##### With Kotlin4MC: the developer does not need Kotlin stdlib to access Unit
```java
// Java using getUnit()
performTask((result) -> {
    player.msg(result);
    return getUnit();
})

// Java using listener()
performTask(
    listener((result) -> player.msg(result))
)
// Listener converts a Java consumer into a Kotlin function
```

##### Without Kotlin4MC: the developer needs Kotlin stdlib to access Unit
```java
// Java using Unit.INSTANCE
performTask((result) -> {
    player.msg(result);
    return Unit.INSTANCE;
})
```

### Simple configuration loading

Load the config from the data folder, otherwise, copy it from the resource "config.yml".

If there is an error, do not continue.

```kotlin
val configFile = dataFolder["config.yml"]
val config = load(configFile, "config.yml")
    ?: return severe("Could not load configuration")
```

You can ommit the resource argument, it will copy the resource

- "config/bungee.yml" if used on BungeeCord

- "config/bukkit.yml" if used on Bukkit

```kotlin
val configFile = dataFolder["config.yml"]
val config = load(configFile)
    ?: return severe("Could not load configuration")
```

### Fast logging

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
val delay = config.getLong("delay").neq(0) // Assignment + Check
    ?: return warning("Delay should not be 0")
```

##### Without Kotlin4MC
```kotlin
val delay = config.getLong("delay")  // Assignment
if(delay == 0) return warning("Delay should not be 0") // Check
```

### Short Unit conversion

You can use .unit after anything to convert it to Unit

```kotlin
interface Tester{
    fun test(): Unit
}

fun task(arg: Argument): Result // ...

class myClass: Tester{
    // throws error
    override fun test(): Unit = task()

    // too verbose
    override fun test(): Unit {
        task()
    }
    
    // correct
    override fun test(): Unit = task().unit
}
```

### How to implement it?

- Gradle: add this to your build.gradle

      repositories {
          maven { url 'https://mymavenrepo.com/repo/NIp3fBk55f5oF6VI1Wso/' }
      }

      dependencies {
          compileOnly 'fr.rhaz.minecraft:kotlin4mc:2.0.1'
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
            <version>2.0.1</version>
            <scope>provided</scope>
        </dependency>
      </dependencies>
