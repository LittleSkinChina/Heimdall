plugins {
    `java-library`
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("org.slf4j:slf4j-api:2.0.17")
    compileOnly("org.apache.logging.log4j:log4j-api:2.25.3")
    implementation("com.squareup.okhttp3:okhttp:5.3.0")
    implementation("dev.rollczi:litecommands-annotations:3.10.9")
    implementation("com.google.code.gson:gson:2.10.1")
    api("ch.jalu:configme:1.4.1")
    api("net.kyori:adventure-api:4.25.0")
    api("net.kyori:adventure-text-minimessage:4.25.0")
}

tasks.test {
    useJUnitPlatform()
}