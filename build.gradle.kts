import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
    kotlin("plugin.jpa") version "1.5.21"

    // query dsl
    id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
    kotlin("kapt") version "1.4.10"

    // jacoco
    jacoco
}

group = "com.hapHollys"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
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
    implementation("org.springframework.boot:spring-boot-starter-batch:_")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // db
    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")

    // test
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito")
    }
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
                exclude("**/Q*", "**/dto/*", "**/for_test/*", "**/batch/*")
            }
        )
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            enabled = true
            // 'element'가 없으면 프로젝트의 전체 파일을 합친 값을 기준으로 합니다.
            limit {
                // 'counter'를 지정하지 않으면 default는 'INSTRUCTION'
                // 'value'를 지정하지 않으면 default는 'COVEREDRATIO'
                minimum = "0.30".toBigDecimal()
            }
        }

        rule {
            enabled = true
            element = "CLASS"
            excludes = listOf(
                "**.batch.*",
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

    dependsOn(":test",
        ":jacocoTestReport",
        ":jacocoTestCoverageVerification")

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}