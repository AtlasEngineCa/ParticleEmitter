plugins {
    id("java")
    `maven-publish`
    signing
}

sourceSets {
    main {
        java {
            srcDir("src/main")
        }
    }
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

description = "Particle Effects for WorldSeed"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.jar {
    manifest {
        archiveBaseName.set("ParticleEffects")
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        groupId = "net.worldseed.particleeffects"
        artifactId = "ParticleEffects"
        version = "1.0"

        from(components["java"])
    }

    repositories {
        maven {
            name = "WorldSeed"
            url = uri("https://reposilite.worldseed.online/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("com.github.Minestom:Minestom:5f8842084c")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}