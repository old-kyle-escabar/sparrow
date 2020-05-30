description = "Deobfuscator"

dependencies {
    api(project(":asm"))
    asm()
    implementation(Library.classgraph)
}