# Kotlin4MC : The ultimate Kotlin library for Minecraft plugin development

For Spigot and BungeeCord

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

### Plugin updates checker

You can check for updates of your plugin using Spiget
![](https://ipfs.io/ipfs/QmZU6234EqPiQVH8Wj6ofkNgsA4UuUTovaMVbp85DPuubF/Capture.PNG)

Just use update() with your Spigot resource ID
```kotlin
update(15938, RED)
```

You can specify the color
```kotlin
update(15938, LIGHT_PURPLE)
```

and the permission (the default permission is "rhaz.update")
```kotlin
update(15938, LIGHT_PURPLE, "myplugin.updates")
```


### How to implement it?

- Gradle: add this to your build.gradle

      repositories {
          maven { url 'https://mymavenrepo.com/repo/NIp3fBk55f5oF6VI1Wso/' }
      }

      dependencies {
          compileOnly 'fr.rhaz.minecraft:kotlin4mc:1.2.61'
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
            <version>1.2.61</version>
            <scope>provided</scope>
        </dependency>
      </dependencies>
