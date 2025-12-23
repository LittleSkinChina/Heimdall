extra.set("targetJava", 17)

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.0")

    implementation(project(":core"))
    implementation("org.bstats:bstats-velocity:3.1.0")
    implementation("dev.rollczi:litecommands-velocity:3.10.9")
    implementation("dev.rollczi:litecommands-adventure:3.10.9")
}
