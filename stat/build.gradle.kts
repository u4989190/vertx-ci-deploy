import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
  import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin ("jvm") version "1.4.10"
  application
  id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "jp.pr"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  jcenter()
}

val kotlinVersion = "1.4.10"
val vertxVersion = "4.0.0.CR1"
val junitJupiterVersion = "5.6.0"

val mainVerticleName = "jp.pr.stat.MainVerticle"
val watchForChange = "src/**/*"
val doOnChange = "./gradlew classes"
val launcherClassName = "io.vertx.core.Launcher"

application {
  mainClassName = launcherClassName
}

dependencies {
  implementation("io.vertx:vertx-config:$vertxVersion")
  implementation("io.vertx:vertx-web-templ-thymeleaf:$vertxVersion")
  implementation("io.vertx:vertx-web:$vertxVersion")
  implementation("io.vertx:vertx-pg-client:$vertxVersion")
  implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion")
  implementation("io.vertx:vertx-lang-kotlin:$vertxVersion")
  implementation(kotlin("stdlib-jdk8"))
  testImplementation("io.vertx:vertx-junit5:$vertxVersion")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

  val compileKotlin: KotlinCompile by tasks
  compileKotlin.kotlinOptions.jvmTarget = "13"

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles {
    include("META-INF/services/io.vertx.core.spi.VerticleFactory")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
