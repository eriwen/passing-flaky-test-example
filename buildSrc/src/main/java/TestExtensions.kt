open class TestExtensions(var repetitions: Int, var isRerunTests: Boolean) {

    fun needsRerunTests(): Boolean {
        return repetitions > 0 || isRerunTests
    }

}
