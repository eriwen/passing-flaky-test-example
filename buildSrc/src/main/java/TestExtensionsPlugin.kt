import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

@Suppress("unused")
class TestExtensionsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val testRepetitions = project.findProperty("testRepetitions")
        val repetitions = testRepetitions?.toString()?.trim { it <= ' ' }?.toInt() ?: 0

        project.extensions.create("testExtensions",
                TestExtensions::class.java,
                repetitions,
                project.hasProperty("testRerun"))

        project.tasks.withType(Test::class.java).configureEach { test ->
            val testExtensions = test.project.extensions.getByType(TestExtensions::class.java)
            if (testExtensions.repetitions > 0) RepeatingTestExecutor.applyTo(test, testExtensions.repetitions)
            if (testExtensions.needsRerunTests()) test.outputs.upToDateWhen { false }
        }
    }
}
