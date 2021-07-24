package com.kaizensundays.particles.websocket.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.web.socket.messaging.SessionConnectedEvent

/**
 * Created: Sunday 5/30/2021, 1:25 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class SessionConnectedEventListener : ApplicationListener<SessionConnectedEvent> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun onApplicationEvent(event: SessionConnectedEvent) {
        logger.info("{}", event)
    }

}