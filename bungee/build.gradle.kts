extra.set("targetJava", 8)

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.13-SNAPSHOT")

    implementation(project(":core"))
    implementation("org.bstats:bstats-bungeecord:3.1.0")
    implementation("net.kyori:adventure-platform-bungeecord:4.4.1")
    implementation("dev.rollczi:litecommands-bungeecord:3.10.9")
    implementation("dev.rollczi:litecommands-adventure-platform:3.10.9")
}
