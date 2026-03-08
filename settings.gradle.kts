pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.inventivetalent.org/repository/public/")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.inventivetalent.org/repository/public/")
    }
}

rootProject.name = "mirage"

include("mirage-core")
include("mirage-platform-spigot")
include("mirage-platform-minestom")

project(":mirage-core").projectDir = file("core")
project(":mirage-platform-spigot").projectDir = file("platform-spigot")
project(":mirage-platform-minestom").projectDir = file("platform-minestom")
