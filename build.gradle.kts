plugins {
    id("java")
}

allprojects {
    group = "cafe.honoka.heimdall"
    version = System.getenv("HEIMDALL_PLUGIN_VERSION") ?: "1.0.0-SNAPSHOT"
    extra.set("pluginId", "heimdall")
    extra.set("author", "LittleSkin")
    extra.set("url", "https://heimdall.honoka.cafe")
    description = "No hacked Minecraft accounts."

    repositories {
        mavenCentral()
        maven {
            name = "spigotmc-repo"
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/public/")
        }
        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }
        maven {
            name = "Minecraft Libraries"
            url = uri("https://libraries.minecraft.net/")
        }
        maven {
            name = "papermc-repo"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
        maven {
            name = "aikar-repo"
            url = uri("https://repo.aikar.co/content/groups/aikar/")
        }
        maven {
            name = "panda-lang-repo"
            url = uri("https://repo.panda-lang.org/releases")
        }
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

subprojects {
    apply(plugin = "java")
    version = rootProject.version
    description = rootProject.description

    afterEvaluate {
        val targetJavaVersion: Int = if (extra.has("targetJava")) {
            extra.get("targetJava").toString().toInt()
        } else {
            8
        }

        tasks.withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
            options.release.set(targetJavaVersion)
            options.compilerArgs.add("-parameters")
        }
    }

    tasks.withType<ProcessResources> {
        val props = mapOf(
            "group" to rootProject.group,
            "name" to rootProject.name,
            "version" to rootProject.version,
            "description" to rootProject.description,
            "pluginId" to rootProject.extra["pluginId"],
            "author" to rootProject.extra["author"],
            "url" to rootProject.extra["url"]
        )
        inputs.properties(props)
        filteringCharset = "UTF-8"

        filesMatching(listOf(
            "plugin.yml",
            "bungee.yml",
            "velocity-plugin.json"
        )) {
            expand(props)
        }
    }
}

tasks.jar {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}