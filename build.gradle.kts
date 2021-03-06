import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt


plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
    kotlin("plugin.jpa") version "1.5.21"

    // all open
    kotlin("plugin.allopen") version "1.4.32"

    // query dsl
    id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
    kotlin("kapt") version "1.4.10"

    // jacoco
    jacoco
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

group = "com.hapHollys"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    // kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // sawgger
    implementation("io.springfox:springfox-swagger2:_")
    implementation("io.springfox:springfox-swagger-ui:_")

    // queryDsl
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.querydsl:querydsl-jpa")
    kapt("com.querydsl:querydsl-apt:4.2.2:jpa")
    annotationProcessor(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa")

    // spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation:_")

    // db
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")

    // logging
    implementation("io.sentry:sentry-spring-boot-starter:_")
    implementation("io.sentry:sentry-logback:_")

    // test
    testImplementation("com.ninja-squad:springmockk:_")
    testImplementation("io.mockk:mockk:_")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito")
    }
}

tasks.jar {
    enabled = false
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// queryDsl
sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

// jacoco
jacoco {
    toolVersion = "0.8.7"
    reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))

        classDirectories.setFrom(
            sourceSets.main.get().output.asFileTree.matching {
                exclude(
                    "**/Q*",
                    "**/dto/*",
                    "**/for_test/*",
                    "**/infra/*",
                    "**/external/*",
                )
            }
        )
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            enabled = true
            // 'element'??? ????????? ??????????????? ?????? ????????? ?????? ?????? ???????????? ?????????.
            limit {
                // 'counter'??? ???????????? ????????? default??? 'INSTRUCTION'
                // 'value'??? ???????????? ????????? default??? 'COVEREDRATIO'
                minimum = "0.30".toBigDecimal()
            }
        }

        rule {
            enabled = true
            element = "CLASS"
            excludes = listOf(
                "**.Q*",
                "**.dto.*",
                "**.for_test.*",
                "**.infra.*",
                "**.external.*",
                "**.Companion",
                "**.*ApplicationKt"
            )

            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.8".toBigDecimal()
            }
        }
    }
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "Runs the unit tests with coverage"

    dependsOn(
        ":test",
        ":jacocoTestReport",
        ":jacocoTestCoverageVerification"
    )

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}
