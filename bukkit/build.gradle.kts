dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8-R0.1-SNAPSHOT")

    implementation(project(":core"))
    implementation("org.bstats:bstats-bukkit:3.1.0")
    implementation("net.kyori:adventure-platform-bukkit:4.4.1")
    implementation("dev.rollczi:litecommands-bukkit:3.10.9")
    implementation("dev.rollczi:litecommands-adventure-platform:3.10.9")
}
