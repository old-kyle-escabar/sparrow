plugins {
    `maven-publish`
}

description = "ASM"

dependencies {
    asm()
    implementation(Library.jgrapht)
}