plugins {
    kotlin("jvm") version Project.kotlinVersion
    application
}

tasks.withType<Wrapper> {
    gradleVersion = Project.gradleVersion
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "io.rsbox"
    version = Project.version

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))

        tinylog()
    }

    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = Project.jvmVersion.toString()
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = Project.jvmVersion.toString()
        }
    }
}

apply(plugin = "application")

tasks.withType<JavaExec> {
    workingDir = rootProject.projectDir
    main = "io.rsbox.sparrow.Sparrow"
}