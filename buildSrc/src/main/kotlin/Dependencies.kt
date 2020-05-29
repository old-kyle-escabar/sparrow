import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Project {
    const val version = "1.0"
    const val kotlinVersion = "1.3.72"
    const val gradleVersion = "6.3"
    val jvmVersion = JavaVersion.VERSION_11
}

object Plugin {

}

object Library {
    private object Version {
        const val tinylog = "2.1.2"
        const val clikt = "2.7.1"
        const val asm = "8.0.1"
    }

    const val tinylogApi = "org.tinylog:tinylog-api-kotlin:${Version.tinylog}"
    const val tinylogImpl = "org.tinylog:tinylog-impl:${Version.tinylog}"
    const val clikt = "com.github.ajalt:clikt:${Version.clikt}"
    const val asm = "org.ow2.asm:asm:${Version.asm}"
    const val asmCommons = "org.ow2.asm:asm-commons:${Version.asm}"
    const val asmUtil = "org.ow2.asm:asm-util:${Version.asm}"
    const val asmTree = "org.ow2.asm:asm-tree:${Version.asm}"
}

fun DependencyHandlerScope.tinylog() {
    "implementation"(Library.tinylogApi)
    "implementation"(Library.tinylogImpl)
}

fun DependencyHandlerScope.asm() {
    "implementation"(Library.asm)
    "implementation"(Library.asmCommons)
    "implementation"(Library.asmTree)
    "implementation"(Library.asmUtil)
}