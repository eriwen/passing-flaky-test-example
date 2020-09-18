plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.3.72"
}

repositories {
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

gradlePlugin {
    plugins {
        create("testExtensions") {
            id = "testextensions"
            implementationClass = "TestExtensionsPlugin"
            description = "Provides repeating test execution and test-reruns"
        }
    }
}
