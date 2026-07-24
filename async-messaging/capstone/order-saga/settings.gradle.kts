// Auto-provision the JDK 21 toolchain (build works even if only another JDK is installed locally).
plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "order-saga"
