plugins {
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "net.azisaba"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://repo2.acrylicstyle.xyz") }
    maven { url = uri("https://libraries.minecraft.net/") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")
    implementation("xyz.acrylicstyle:sequelize4j:0.5.3")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.7.3")
    compileOnly("org.spigotmc:spigot:1.17.1-R0.1-SNAPSHOT")
}
