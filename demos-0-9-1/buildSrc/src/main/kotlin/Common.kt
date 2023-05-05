import org.jetbrains.kotlin.gradle.dsl.KotlinJsProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithJsPresetFunctions
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithPresetFunctions
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.js.webpack.WebpackDevtool

private fun KotlinJsTargetDsl.configure() {
    compilations.all {
        kotlinOptions {
            moduleKind = "umd"
            sourceMapEmbedSources = "always"
            freeCompilerArgs = listOf("-Xuse-experimental=kotlin.ExperimentalUnsignedTypes")
        }
    }
    browser {
        testTask {
            enabled = false
        }
    }
}


fun KotlinJsProjectExtension.jsTargets(compiler: KotlinJsCompilerType = defaultJsCompilerType) {
    js(compiler).configure()
}


fun KotlinTargetContainerWithJsPresetFunctions.jsTargets(compiler: KotlinJsCompilerType = defaultJsCompilerType) {
    js(compiler).configure()
}


fun KotlinTargetContainerWithPresetFunctions.jvmTargets(jvmTarget: String = "11") {
    jvm {
        compilations.all {
            kotlinOptions {
                this.jvmTarget = jvmTarget
                freeCompilerArgs = listOf("-Xuse-experimental=kotlin.ExperimentalUnsignedTypes")
            }
        }
    }
}


fun KotlinMultiplatformExtension.jsTargets() {
    js {
        val releaseBuild = project.hasProperty("release")

        compilations.all {
            kotlinOptions {
                sourceMap = !releaseBuild
                if (sourceMap) {
                    sourceMapEmbedSources = "always"
                }
                moduleKind = "umd"
                freeCompilerArgs = listOf("-opt-in=kotlin.ExperimentalUnsignedTypes")
            }
        }
        browser {
            testTask {
                enabled = false
            }
        }
    }
}


@OptIn(ExperimentalDistributionDsl::class)
fun KotlinMultiplatformExtension.jsTargetsWithWebpack() {
    js {
        compilations.all {
            kotlinOptions {
                sourceMap = true
                sourceMapEmbedSources = "always"
                moduleKind = "umd"
                freeCompilerArgs = listOf("-opt-in=kotlin.ExperimentalUnsignedTypes")
                metaInfo = true
                verbose = false
            }
        }
        browser {
            /*
            // This write EVERYTHING to ${project.buildDir.path}/distributions/assets/js/assets
            distribution {
                println("BROWSER DIST DIR:  ${project.buildDir.path}/distributions/assets/js")
                directory = File("${project.buildDir.path}/distributions/assets/js")
            }
             */
            webpackTask {
                // This does nothing
                // println("WEBPACK TASK destinationDirectory:  ${project.buildDir.path}/distributions/assets/js")
                // destinationDirectory = File("${project.buildDir.path}/distributions/assets/js")
                cssSupport {
                    enabled.set(true)
                }
            }
            commonWebpackConfig {
                // This does nothing.
                // println("WEBPACK DEST:  ${project.buildDir.path}/distributions/assets/js")
                // outputPath = File("${project.buildDir.path}/distributions/assets/js")
                //
                // DevServer:  see https://webpack.js.org/configuration/dev-server
                // Tells dev-server to open the browser after server had been started.
                // Set it to true to open your default browser.
                devServer?.`open` = false
                // Specify a port number to listen for requests on.
                devServer?.`port` = 8080
                mode = KotlinWebpackConfig.Mode.DEVELOPMENT
                devtool = WebpackDevtool.EVAL_SOURCE_MAP
                sourceMaps = true
                showProgress = true
            }
            testTask {
                enabled = false
            }
            binaries.executable()
        }
    }
}

fun KotlinMultiplatformExtension.jvmTargets(jvmTarget: String = "11") {
    jvm {
        withJava()
        jvmToolchain(jvmTarget.toInt())
        compilations.all {
            kotlinOptions {
                this.jvmTarget = jvmTarget
                freeCompilerArgs = listOf("-opt-in=kotlin.ExperimentalUnsignedTypes")
            }
        }
    }
}
