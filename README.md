# Kotlin4MC
Kotlin for Minecraft

### Implement it

- Gradle: add this to your build.gradle

      repositories {
          maven { url 'https://mymavenrepo.com/repo/NIp3fBk55f5oF6VI1Wso/' }
      }

      dependencies {
          compile 'fr.rhaz.minecraft:kotlin4mc:1.2.60'
      }

      jar {
          from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }
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
            <version>1.2.60</version>
            <scope>compile</scope>
        </dependency>
      </dependencies>
