package com.kaizensundays.particles.websocket.service.stomp

/**
 * Created: Monday 5/31/2021, 1:18 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
data class Quote(
    val symbol: String, val sequence: Long = 0,
    val bidPrice: Double = 0.0, val askPrice: Double = 0.0
) : JacksonSerializableUntyped