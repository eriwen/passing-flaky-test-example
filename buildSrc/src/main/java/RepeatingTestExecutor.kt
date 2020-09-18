import org.gradle.api.internal.tasks.testing.JvmTestExecutionSpec
import org.gradle.api.internal.tasks.testing.TestExecuter
import org.gradle.api.internal.tasks.testing.TestResultProcessor
import org.gradle.api.tasks.testing.Test
import java.util.*

internal class RepeatingTestExecutor(private val delegate: TestExecuter<JvmTestExecutionSpec>, private val repetitions: Int) : TestExecuter<JvmTestExecutionSpec> {
    override fun execute(testExecutionSpec: JvmTestExecutionSpec, testResultProcessor: TestResultProcessor) {
        val forkOptions = testExecutionSpec.javaForkOptions
        val savedSystemProps: Map<String, Any?> = HashMap(forkOptions.systemProperties)
        try {
            for (i in 0 until repetitions) {
                delegate.execute(testExecutionSpec, testResultProcessor)
            }
        } finally {
            forkOptions.systemProperties = savedSystemProps
        }
    }

    override fun stopNow() {
        delegate.stopNow()
    }

    companion object {
        fun applyTo(test: Test, repetitions: Int) {
            setTestExecutor(test, RepeatingTestExecutor(getOrCreateTestExecutor(test), repetitions))
        }

        private fun getOrCreateTestExecutor(test: Test): TestExecuter<JvmTestExecutionSpec> {
            return try {
                val createTestExecuter = Test::class.java.getDeclaredMethod("createTestExecuter")
                createTestExecuter.isAccessible = true
                @Suppress("UNCHECKED_CAST")
                createTestExecuter.invoke(test) as TestExecuter<JvmTestExecutionSpec>
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

        private fun setTestExecutor(test: Test, testExecuter: TestExecuter<JvmTestExecutionSpec>) {
            try {
                val setTestExecuter = Test::class.java.getDeclaredMethod("setTestExecuter", TestExecuter::class.java)
                setTestExecuter.isAccessible = true
                setTestExecuter.invoke(test, testExecuter)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

}
