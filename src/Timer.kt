import java.text.DecimalFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random
import kotlin.reflect.KClass

object Timer {
    private val map: MutableMap<String, Long> = mutableMapOf()

    fun time(block: ()-> Any) {
        val name = Random.nextLong().toString()
        start(name)
        block()
        stop(name)
    }

    fun start(name: String) {
        map[name] = System.currentTimeMillis()
    }

    fun stop(name: String) {
        println(
            formatMillisecondsAsSeconds(
                System.currentTimeMillis() - map[name]!!
            )
        )
    }

    private fun formatMillisecondsAsSeconds(milliseconds: Long): String {
        val seconds = milliseconds / 1000.0
        val decimalFormat = DecimalFormat("#.###")
        return decimalFormat.format(seconds) + "s"
    }
}