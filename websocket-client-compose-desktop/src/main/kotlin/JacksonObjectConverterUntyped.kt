import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

/**
 * Created: Sunday 4/18/2021, 6:28 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
open class JacksonObjectConverterUntyped : ObjectConverterUntyped<String> {

    private val jackson = ObjectMapper().registerModule(KotlinModule())

    override fun <U> fromObject(obj: U): String {
        try {
            return jackson.writeValueAsString(obj)
        } catch (e: Exception) {
            throw IllegalStateException()
        }
    }

    override fun <U> toObject(wire: String, type: Class<U>): U {
        try {
            @Suppress("UNCHECKED_CAST")
            return jackson.readValue(wire, type) as U
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
    }

}