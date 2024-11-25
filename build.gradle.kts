plugins {
	java
	id("org.springframework.boot") version "3.3.6"
	id("io.spring.dependency-management") version "1.1.6"
	id("com.vaadin") version "24.5.5"
}

group = "com.opm"
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
	maven { url = uri("https://maven.vaadin.com/vaadin-addons") }
	maven { url = uri("https://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts/") }
}

extra["vaadinVersion"] = "24.5.5"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.vaadin:vaadin-spring-boot-starter")
	implementation("org.apache.poi:poi:5.2.5")
	implementation("org.vaadin.reportui:report-ui:4.0.0")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
