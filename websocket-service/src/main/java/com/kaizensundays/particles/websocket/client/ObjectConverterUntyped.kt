package com.kaizensundays.particles.websocket.client

/**
 * Created: Sunday 4/18/2021, 6:29 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
interface ObjectConverterUntyped<W> {

    fun <U> fromObject(obj: U): W

    fun <U> toObject(wire: W, type: Class<U>): U

}