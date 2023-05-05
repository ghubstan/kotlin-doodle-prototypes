plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenLocal()
}

val kotlinVersion = "1.8.20"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation(gradleApi())
}
