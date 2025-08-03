plugins {
    java
    checkstyle
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
    implementation(libs.springBootStarter.redis)

    developmentOnly(libs.springBoot.devtools)

    runtimeOnly(libs.h2)
    runtimeOnly(libs.postgresql)

    compileOnly(libs.lombok)
    compileOnly(libs.mapstruct)

    annotationProcessor(libs.lombok)
    annotationProcessor(libs.mapstruct.annotationProcessor)
    annotationProcessor(libs.lombok.mapstructBinding)

    testImplementation(libs.springBootStarter.test)
    testImplementation(platform(libs.junit.bom))
    testRuntimeOnly(libs.junit.platformLauncher)
    testImplementation(libs.javacrumbs)
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

checkstyle {
    toolVersion = "10.26.1"
    configFile = file("$projectDir/checkstyle/hexlet-checks.xml")
}


tasks.withType<Test> {
    useJUnitPlatform()
}
