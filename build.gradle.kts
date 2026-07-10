import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    id("java")
    // Kotlin 2.3.20 is fully supported on Gradle 9.5.x.
    id("org.jetbrains.kotlin.jvm") version "2.3.20"
    // The 2.x line supports Gradle 9 and IntelliJ Platform 2024.2+.
    id("org.jetbrains.intellij.platform") version "2.18.0"
}

group = "org.nomolang"
version = "0.1.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
        // Required so that instrumentationTools() (the Java compiler used by
        // the instrumentCode task) can be resolved.
        intellijDependencies()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2")
        // LSP4IJ provides the generic LSP client used to talk to nomo-lsp.
        plugin("com.redhat.devtools.lsp4ij:0.20.1")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "242"
            untilBuild = provider { null }
        }
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        channels = listOf("default")
    }

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    pluginVerification {
        ides {
            current()
            create(IntelliJPlatformType.IntellijIdea, "2026.1.4")
        }
    }
}

kotlin {
    // IntelliJ Platform 2024.2 requires sourceCompatibility 21. The JDK is
    // resolved automatically via the foojay-resolver configured in settings.gradle.kts.
    jvmToolchain(21)
}

// Pin the Gradle wrapper version so any `wrapper` task run keeps the project on the
// version that is validated against IntelliJ Platform Gradle Plugin 2.16.0.
tasks.wrapper {
    gradleVersion = "9.5.1"
    distributionType = Wrapper.DistributionType.BIN
}
