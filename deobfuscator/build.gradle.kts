description = "Deobfuscator"

dependencies {
    asm()
    implementation(Library.classgraph)
    implementation(Library.jgrapht)
    implementation(Library.guava)
}