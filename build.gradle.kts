plugins {
    java
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
}

group = "ru.anseranser"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.springBootStarter.web)
    implementation(libs.springBootStarter.actuator)
    implementation(libs.springBootStarter.validation)
    implementation(libs.springBootStarter.dataJpa)

    developmentOnly(libs.springBoot.devtools)

    runtimeOnly(libs.h2)
    runtimeOnly(libs.postgresql)

    compileOnly(libs.lombok)
    compileOnly(libs.mapstruct)

    annotationProcessor(libs.lombok)
    annotationProcessor(libs.mapstruct.annotationProcessor)
    annotationProcessor(libs.lombok.mapstructBinding)

    testImplementation(libs.springBootStarter.test)
    testImplementation(libs.javacrumbs)
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.withType<Test> {
    useJUnitPlatform()
}
