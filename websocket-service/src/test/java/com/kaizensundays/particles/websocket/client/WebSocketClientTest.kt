package com.kaizensundays.particles.websocket.client

import org.junit.Ignore
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Thread.sleep
import kotlin.test.assertTrue

/**
 * Created: Monday 5/31/2021, 11:24 AM Eastern Time
 *
 * @author Sergey Chuykov
 */
@Ignore
class WebSocketClientTest {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val client = WebSocketClient()

    @Test
    fun test() {

        val handler = QuoteMessageHandler()

        val session = client.connect()

        if (session != null && client.connected.get()) {
            logger.info("Connected")

            sleep(1000)

            client.subscribe(session, handler)

            sleep(10000)

            session.disconnect()
        }

        assertTrue(handler.counter.get() >= 3)

        logger.info("Done")
    }

}