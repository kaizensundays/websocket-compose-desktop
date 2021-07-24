import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import java.lang.reflect.Type

/**
 * Created: Monday 7/5/2021, 1:55 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class QuoteMessageHandler(private val listener: QuoteListener) : StompFrameHandler {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val converter = JacksonObjectConverterUntyped()

    override fun getPayloadType(headers: StompHeaders): Type {
        return ByteArray::class.java
    }

    override fun handleFrame(headers: StompHeaders, payload: Any?) {
        if (payload is ByteArray) {
            val wire = String(payload)
            val quote = converter.toObject(wire, Quote::class.java)
            logger.info("{}", quote)
            listener.onQuote(quote)
        }
    }

}