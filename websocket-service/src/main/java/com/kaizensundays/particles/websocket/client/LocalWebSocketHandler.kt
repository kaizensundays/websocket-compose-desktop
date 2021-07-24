package com.kaizensundays.particles.websocket.client

import org.slf4j.Logger
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

/**
 * Created: Saturday 7/10/2021, 2:16 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@Deprecated("")
class LocalWebSocketHandler(private val logger: Logger) : TextWebSocketHandler() {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("* {}", session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info("* {} {}", session, status)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info("* {}", message)
    }
}
