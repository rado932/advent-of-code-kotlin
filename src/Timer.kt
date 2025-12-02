import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

object Timer {
    @OptIn(ExperimentalTime::class)
    fun time(block: () -> Any) {
        println(measureTime { block() })
    }
}