description = "Mapper"

dependencies {
    asm()
    implementation(Library.guava)
    api(project(":asm"))
}