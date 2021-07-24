package com.kaizensundays.particles.websocket.service

import org.springframework.context.support.ClassPathXmlApplicationContext

/**
 * Created: Saturday 5/22/2021, 1:44 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        val context = ClassPathXmlApplicationContext("/tomcat.xml")
        context.registerShutdownHook()

    }

}