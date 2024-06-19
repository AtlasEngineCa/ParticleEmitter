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
}

description = "Particle Effects for WorldSeed"
java.sourceCompatibility = JavaVersion.VERSION_21

tasks.jar {
    manifest {
        archiveBaseName.set("ParticleEmitter")
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        groupId = "net.worldseed.particleemitter"
        artifactId = "ParticleEmitter"
        version = "1.3.35"

        from(components["java"])
    }

    repositories {
        maven {
            name = "WorldSeed"
            url = uri("https://reposilite.worldseed.online/public")
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

    compileOnly("net.minestom:minestom-snapshots:f1d5940855")
    testImplementation("net.minestom:minestom-snapshots:f1d5940855")

    implementation("dev.hollowcube:mql:1.0.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}