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
    maven("https://jitpack.io")
}

description = "Particle Effects for WorldSeed"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.jar {
    manifest {
        archiveBaseName.set("ParticleEmitter")
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        groupId = "net.worldseed.particleemitter"
        artifactId = "ParticleEmitter"
        version = "1.3.31"

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
    compileOnly("com.github.Minestom:Minestom:8ad2c7701f")
    testImplementation("com.github.Minestom:Minestom:8ad2c7701f")
    implementation("com.github.hollow-cube.common:mql:117d7c64b1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}