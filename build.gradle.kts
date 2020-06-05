buildscript {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Plugins.kotlinGradlePlugin)
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
    id("org.jetbrains.kotlin.kapt") version kotlinVersion
    id("io.gitlab.arturbosch.detekt") version "1.0.1"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

allprojects {

    version = "0.0.1"
    group = "gradle-integration-tests"

    repositories {
        jcenter()
        mavenCentral()
    }
}

subprojects {

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    dependencies {

        implementation(platform("org.jetbrains.kotlin:kotlin-bom:$kotlinVersion"))
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        testImplementation("org.jetbrains.kotlin:kotlin-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-params:$jupiterVersion")
        testImplementation("org.assertj:assertj-core:3.12.2")

        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
    }

    tasks {

        compileKotlin {
            kotlinOptions {
                jvmTarget = "11"
                javaParameters = true
            }
        }

        compileTestKotlin {
            kotlinOptions {
                jvmTarget = "11"
                javaParameters = true
            }
        }

        test {
            exclude("**/*Integration*")
        }

        task<Test>("integrationTest") {
            description = "Runs integration tests."
            group = "verification"
            testClassesDirs = sourceSets.test.get().output.classesDirs
            classpath = sourceSets.test.get().runtimeClasspath

            include("**/*Integration*")
            shouldRunAfter("test")
        }

        check {
            dependsOn("integrationTest")
        }
    }

    tasks.withType(Test::class.java) {
        useJUnitPlatform()
    }
}
