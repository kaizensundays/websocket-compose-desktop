package com.kaizensundays.particles.websocket.client

import com.kaizensundays.particles.websocket.service.stomp.Subscribe
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.Transport
import org.springframework.web.socket.sockjs.client.WebSocketTransport
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created: Saturday 7/10/2021, 2:15 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class WebSocketClient : StompSessionHandlerAdapter() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val converter = JacksonObjectConverterUntyped()

    val connected = AtomicBoolean()

    override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
        logger.info("afterConnected: {} {}", session, connectedHeaders)
        connected.set(true)
    }

    override fun handleTransportError(session: StompSession, exception: Throwable) {
        logger.info("handleTransportError: {} {}", session, exception)
        connected.set(false)
    }

    fun connect(): StompSession? {

        val transport: Transport = WebSocketTransport(StandardWebSocketClient())
        val transports = mutableListOf(transport)

        val sockJsClient = SockJsClient(transports)
        sockJsClient.messageCodec = Jackson2SockJsMessageCodec()

        val stompClient = WebSocketStompClient(sockJsClient)

        val f = stompClient.connect("ws://localhost:8080/endpoint", this)

        try {
            val session = f.get(3000, TimeUnit.MILLISECONDS)
            if (session != null) {
                connected.set(true)
            }
            return session
        } catch (e: Exception) {
            logger.error(e.message, e)
        }

        return null
    }

    fun subscribe(session: StompSession, handler: QuoteMessageHandler) {

        session.subscribe("/topic/quote", handler)

        val request = converter.fromObject(Subscribe("AMZN"))

        val receipt = session.send("/app/subscribe", request.toByteArray())
        logger.info("{}", receipt)

    }

}