plugins {
    id("com.gradleup.shadow") version "9.3.0"
}

extra.set("targetJava", 17)

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(project(":bukkit"))
    implementation(project(":bungee"))
    implementation(project(":core"))
    implementation(project(":velocity"))
}

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    manifest {
        attributes(
            "Implementation-Title" to rootProject.name,
            "Implementation-Version" to rootProject.version
        )
    }

    val prefix = "${rootProject.group}.libs"

    relocate("okhttp3", "$prefix.okhttp3")
    relocate("okio", "$prefix.okio")
    relocate("ch.jalu.configme", "$prefix.configme")
    relocate("org.bstats", "$prefix.bstats")
    relocate("dev.rollczi.litecommands", "$prefix.litecommands")
    relocate("com.google.gson", "$prefix.gson")
    relocate("org.yaml.snakeyaml", "$prefix.snakeyaml")

    archiveBaseName.set(rootProject.name.toString())
    archiveClassifier.set("")
    archiveVersion.set(rootProject.version.toString())
}

tasks.test {
    useJUnitPlatform()
}