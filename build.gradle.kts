plugins {
    application
    checkstyle
    jacoco
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.sonarCube)
}

group = "ru.anseranser"
version = "0.0.1-SNAPSHOT"

application {
    mainClass = "ru.anseranser.brevis.BrevisApplication"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

checkstyle {
    toolVersion = "10.26.1"
    configFile = file("${rootDir}/checkstyle/sun_checks_hexlet_edition.xml")
    isIgnoreFailures = true
    isShowViolations = true
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
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

tasks.jacocoTestReport {
    reports {
        xml.required = true
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

sonar {
    properties {
        property("sonar.projectKey", "sergeloie_brevis")
        property("sonar.organization", "sergeloie")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
