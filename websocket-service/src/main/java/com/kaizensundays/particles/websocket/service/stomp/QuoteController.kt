package com.kaizensundays.particles.websocket.service.stomp

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Created: Monday 5/31/2021, 1:20 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
@Controller
class QuoteController : ApplicationListener<SessionDisconnectEvent> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var template: SimpMessagingTemplate

    private var executor = Executors.newScheduledThreadPool(1)

    private var scheduledFutures = mutableListOf<ScheduledFuture<*>>()

    private val prices = mapOf(
        "AMZN" to 3573.0,
        "AAPL" to 146.0,
        "ORCL" to 87.0
    )

    private val sequences = mapOf(
        "AMZN" to AtomicLong(),
        "AAPL" to AtomicLong(),
        "ORCL" to AtomicLong()
    )

    private val random = Random()

    private fun getQuote(symbol: String, prices: Map<String, Double>): Quote? {
        val price = prices[symbol]
        if (price != null) {
            val sequence = sequences[symbol]?.incrementAndGet() ?: 0
            val midPrice = random.nextDouble() + price
            return Quote(symbol, sequence, midPrice - 0.01, midPrice + 0.01)
        }
        return null
    }

    private fun sendQuote(symbol: String) {
        val quote = getQuote(symbol, prices)
        if (quote != null) {
            logger.info("{}", quote)
            template.convertAndSend("/topic/quote", quote)
        }
    }

    @MessageMapping("/subscribe")
    fun subscribe(@Payload request: Subscribe) {
        logger.info("subscribe: {}", request)

        val scheduledFuture = executor.scheduleWithFixedDelay({ sendQuote(request.symbol) }, 3, 3, TimeUnit.SECONDS)
        scheduledFutures.add(scheduledFuture)
    }

    fun unsubscribe() {

        scheduledFutures.forEach { it.cancel(false) }

        sequences.forEach { e -> e.value.set(0) }
    }

    override fun onApplicationEvent(event: SessionDisconnectEvent) {
        logger.info("{}", event)

        unsubscribe()
    }

    @PostConstruct
    fun start() {

        logger.info("Started")
    }

    @PreDestroy
    fun stop() {

        logger.info("Stopped")
    }

}