package com.kaizensundays.particles.websocket.client

import com.kaizensundays.particles.websocket.service.stomp.Quote
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.stomp.StompFrameHandler
import org.springframework.messaging.simp.stomp.StompHeaders
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created: Monday 7/5/2021, 1:55 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class QuoteMessageHandler : StompFrameHandler {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val converter = JacksonObjectConverterUntyped()

    val counter = AtomicInteger()

    override fun getPayloadType(headers: StompHeaders): Type {
        return ByteArray::class.java
    }

    override fun handleFrame(headers: StompHeaders, payload: Any?) {
        if (payload is ByteArray) {
            val wire = String(payload)
            val quote = converter.toObject(wire, Quote::class.java)
            logger.info("{}", quote)
            counter.incrementAndGet()
        }
    }

}