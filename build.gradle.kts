import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.gradle.ext.runConfigurations
import org.jetbrains.gradle.ext.settings
import org.jetbrains.gradle.ext.Gradle

plugins {
	id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.10"
	id("com.gtnewhorizons.retrofuturagradle") version "1.4.5"
	id("com.github.gmazzo.buildconfig") version "5.6.6"
	id("io.freefair.lombok") version "8.14"
}

group = "dev.redstudio"
version = "1.6" // Versioning must follow Ragnarök versioning convention: https://github.com/Red-Studio-Ragnarok/Commons/blob/main/Ragnar%C3%B6k%20Versioning%20Convention.md

val id = project.name.lowercase()
val plugin = "${project.group}.${id}.asm.${project.name}Plugin"

val redCoreVersion = "1.8-1.12-" + "0.6"

val mixinBooterVersion = "10.6"

minecraft {
	mcVersion = "1.12.2"
	username = "Desoroxxx"
	extraRunJvmArguments = listOf("-Dforge.logging.console.level=debug", "-Dfml.coreMods.load=${plugin}", "-Dmixin.hotSwap=true", "-Dmixin.checks.mixininterfaces=true", "-Dmixin.debug.export=true")
}

repositories {
	arrayOf("Release", "Beta", "Dev").forEach { repoType ->
		maven {
			name = "Red Studio - $repoType"
			url = uri("https://repo.redstudio.dev/${repoType.lowercase()}")
			content {
				includeGroup("dev.redstudio")
			}
		}
	}

	maven {
		name = "Cleanroom"
		url = uri("https://maven.cleanroommc.com")
		content {
			includeGroup("zone.rong")
		}
	}

	exclusiveContent {
		forRepository {
			maven {
				name = "Curse Maven"
				url = uri("https://cursemaven.com")
			}
		}
		filter {
			includeGroup("curse.maven")
		}
	}
}

dependencies {
	//implementation("dev.redstudio:Red-Core-MC:$redCoreVersion")

	compileOnly(rfg.deobf("curse.maven:dynamic-lights-227874:2563244"))

	annotationProcessor("org.ow2.asm:asm-debug-all:5.2")
	annotationProcessor("com.google.guava:guava:32.1.2-jre")
	annotationProcessor("com.google.code.gson:gson:2.8.9")

	val mixinBooter: String = modUtils.enableMixins("zone.rong:mixinbooter:$mixinBooterVersion", "mixins.${id}.refmap.json") as String
	api(mixinBooter) {
		isTransitive = false
	}
	annotationProcessor(mixinBooter) {
		isTransitive = false
	}
}

buildConfig {
	packageName("${project.group}.${id}")
	className("ProjectConstants")
	documentation.set("This class defines constants for ${project.name}.\n<p>\nThey are automatically updated by Gradle.")
	useJavaOutput()

	// Details
	buildConfigField("ID", id)
	buildConfigField("NAME", project.name)
	buildConfigField("VERSION", project.version.toString())

	// Loggers
	buildConfigField("org.apache.logging.log4j.Logger", "LOGGER", "org.apache.logging.log4j.LogManager.getLogger(NAME)")
	//buildConfigField("dev.redstudio.redcore.logging.RedLogger", "RED_LOGGER", """new RedLogger(NAME, "https://linkify.cz/AlfheimBugReport", LOGGER)""")
}

// Set the toolchain version to decouple the Java we run Gradle with from the Java used to compile and run the mod
java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(8))
		vendor.set(JvmVendorSpec.ADOPTIUM)
	}
	if (!project.version.toString().contains("Dev"))
		withSourcesJar() // Generate sources jar, for releases
}

tasks {
	arrayOf(deobfuscateMergedJarToSrg, srgifyBinpatchedJar).forEach {
		it.configure {
			accessTransformerFiles.from(project.files("src/main/resources/META-INF/${id}_at.cfg"))
		}
	}

	processResources {
		val expandProperties = mapOf(
			"version" to project.version,
			"name" to project.name,
			"id" to id
		)

		inputs.properties(expandProperties)

		filesMatching("**/*.*") {
			if (!path.endsWith(".png")) {
				if (path.endsWith("mixins.alfheim.json")) {
					filter { line ->
						expandProperties.entries.fold(line) { acc, (key, value) ->
							acc.replace("\${$key}", value.toString())
						}
					}
				} else {
					expand(expandProperties)
				}
			}
		}
	}

	withType<Jar> {
		manifest {
			attributes(
				"ModSide" to "BOTH",
				"FMLAT" to "${id}_at.cfg",
				"FMLCorePlugin" to plugin,
				"FMLCorePluginContainsFMLMod" to "true",
				"ForceLoadAsMod" to "true"
			)
		}
	}

	withType<JavaCompile> {
		options.encoding = "UTF-8"

		options.isFork = true
		options.forkOptions.jvmArgs = listOf("-Xmx4G", "-XX:+UseStringDeduplication")
	}
}

idea {
	module {
		inheritOutputDirs = true
		excludeDirs.addAll(setOf(".github", ".gradle", ".idea", "build", "gradle", "run").map(::file))
	}

	project {
		settings {
			jdkName = "1.8"
			languageLevel = IdeaLanguageLevel("JDK_1_8")

			runConfigurations {
				arrayOf("Client", "Server", "Obfuscated Client", "Obfuscated Server", "Vanilla Client", "Vanilla Server").forEach { name ->
					create(name, Gradle::class.java) {
						val prefix = name.substringBefore(" ").let { if (it == "Obfuscated") "Obf" else it }
						val suffix = name.substringAfter(" ").takeIf { it != prefix } ?: ""
						taskNames = setOf("run$prefix$suffix")

						jvmArgs = "-XX:+UseStringDeduplication"
					}
				}
			}
		}
	}
}
