plugins {
    kotlin("jvm") version "1.9.21"
    id("java")
    id("org.jsonschema2pojo") version "1.2.1"
}

group = "nl.testbeacon.kotlin.rest-assured"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("com.willowtreeapps.assertk:assertk:0.28.0")
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.rest-assured:kotlin-extensions:5.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.1")
}

jsonSchema2Pojo {
    targetPackage = "generated.response"
    includeAdditionalProperties = false
    setSource(files("$projectDir/src/test/resources/schema"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}