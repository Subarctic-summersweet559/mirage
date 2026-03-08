plugins {
    id("java")
    id("java-library")
}

val smolderUsername = providers.gradleProperty("smolderUsername").orNull
val smolderPassword = providers.gradleProperty("smolderPassword").orNull

val repositoryName: String by project
val snapshotRepository: String by project
val releaseRepository: String by project

repositories {
    mavenLocal()
    mavenCentral()

    maven(snapshotRepository) {
        name = "smolderSnapshots"
        if (!smolderUsername.isNullOrBlank() && !smolderPassword.isNullOrBlank()) {
            credentials {
                username = smolderUsername
                password = smolderPassword
            }
        }
    }
    maven(releaseRepository) {
        name = "smolderReleases"
        if (!smolderUsername.isNullOrBlank() && !smolderPassword.isNullOrBlank()) {
            credentials {
                username = smolderUsername
                password = smolderPassword
            }
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}
