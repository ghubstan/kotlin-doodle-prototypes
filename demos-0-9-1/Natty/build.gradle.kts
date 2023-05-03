plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    application
}


/*
TODO I want to fully migrate to IR, but having a lot problems,
        the biggest one being unable to reference anything 'nacular'.
        But small bits are being adjusted.
        See https://slack-chats.kotlinlang.org/t/461144/i-started-a-new-project-in-intellij-multiplatform-full-stack
 */
kotlin {


    jsTargetsWithWebpack()
    jvmTargets()

    val coroutinesVersion: String by project
    val doodleVersion: String by project
    val junitVersion: String by project
    val kodeinVersion: String by project
    val ktorVersion: String by project
    val kotlinxHtmlJvmVersion: String by project
    val logbackVersion: String by project
    val mockkVersion: String by project
    val serializationVersion: String by project
    val slf4jVersion: String by project

    sourceSets {

        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.467")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.467")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.10.5-pre.467")

                implementation("io.nacular.doodle:browser:$doodleVersion")
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")


                implementation("io.nacular.doodle:core:$doodleVersion")
                implementation("io.nacular.doodle:controls:$doodleVersion")
                implementation("io.nacular.doodle:animation:$doodleVersion")
                implementation("io.nacular.doodle:themes:$doodleVersion")

                implementation("org.kodein.di:kodein-di:$kodeinVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-server-compression:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:2.2.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-server-cors:$ktorVersion")
                implementation("io.ktor:ktor-server-html-builder-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")

                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlJvmVersion")
            }
        }

        val jvmTest by getting

        js().compilations["main"].defaultSourceSet {
            dependencies {
                api("org.kodein.di:kodein-di:$kodeinVersion")
            }
        }

        js().compilations["test"].defaultSourceSet {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        jvm().compilations["test"].defaultSourceSet {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:$junitVersion")
                implementation(kotlin("test-junit"))

                implementation("org.slf4j:slf4j-api:$slf4jVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("io.mockk:mockk:$mockkVersion")
            }
        }
    }

}

application {
    mainClass.set("io.dongxi.Server.kt")
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    into("") {
        from(jsBrowserDistribution)
    }
}

/*
tasks.named<Distribution>("jsBrowserDistribution") {
    println("TASK: jsBrowserDistribution")
    doFirst {
        // Just use Java NIO to move the file instead of messing around with ant.groovy.
        val jsFile = Paths.get("${project.buildDir.path}/distributions/natty.js")
        val destDir = Paths.get("${project.buildDir.path}/distributions/assets/js/natty.js")
        Files.move(jsFile, destDir, StandardCopyOption.REPLACE_EXISTING)
    }
}
*/


tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}
