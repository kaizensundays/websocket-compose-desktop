package com.kaizensundays.particles.websocket.service.tomcat

import com.kaizensundays.particles.websocket.service.Main
import org.apache.catalina.core.StandardContext
import org.apache.catalina.startup.Tomcat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

/**
 * Created: Saturday 5/22/2021, 1:51 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class EmbeddedTomcat {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val tomcat = Tomcat()

    var webContentFolder = ""

    var webBaseFolder = ""

    var port = ""

    fun start() {

        System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true")

        tomcat.setPort(Integer.parseInt(port))
        tomcat.setBaseDir(webBaseFolder)

        val ctx = tomcat.addWebapp("", File(webContentFolder).absolutePath) as StandardContext

        ctx.parentClassLoader = Main::class.java.classLoader

        tomcat.start()

        tomcat.server.await()

        logger.info("Started")
    }

    fun stop() {

        logger.info("Stopped")
    }

}