import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.0"
}

group = "com.kaizensundays.particles"
version = "1.0.0-SNAPSHOT"

val slf4jVersion = "1.7.25"
val log4jVersion = "2.13.3"
val springVersion = "5.2.12.RELEASE"
val jacksonVersion = "2.11.3"
val tomcatVersion = "8.5.12"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation(compose.desktop.currentOs)
    implementation("org.slf4j", "slf4j-api", slf4jVersion)
    implementation("org.apache.logging.log4j", "log4j-core", log4jVersion)
    implementation("org.apache.logging.log4j", "log4j-jcl", log4jVersion)
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", log4jVersion)
    implementation("org.springframework", "spring-messaging", springVersion)
    implementation("org.springframework", "spring-websocket", springVersion)
    implementation("org.apache.tomcat.embed", "tomcat-embed-websocket", tomcatVersion)
    implementation("com.fasterxml.jackson.core", "jackson-databind", jacksonVersion)
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion)
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "QuoteViewMain"
/*
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "websocket-client-compose-desktop"
        }
*/
    }
}