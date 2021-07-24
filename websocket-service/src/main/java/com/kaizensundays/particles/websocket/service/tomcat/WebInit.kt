package com.kaizensundays.particles.websocket.service.tomcat

import org.apache.catalina.servlets.DefaultServlet
import org.springframework.web.WebApplicationInitializer
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.XmlWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet
import javax.servlet.ServletContext

/**
 * Created: Saturday 5/22/2021, 1:51 PM Eastern Time
 *
 * @author Sergey Chuykov
 */
class WebInit : WebApplicationInitializer {

    override fun onStartup(container: ServletContext) {

        val context = XmlWebApplicationContext()
        context.setConfigLocation("classpath:service.xml")

        val contextLoaderListener = ContextLoaderListener(context)
        container.addListener(contextLoaderListener)

        val dispatcher = container.addServlet("dispatcher", DispatcherServlet(context))
        dispatcher.setLoadOnStartup(1)
        dispatcher.addMapping("/")

        val defaultServlet = container.addServlet("static", DefaultServlet())
        defaultServlet.setInitParameter("debug", "1")
        defaultServlet.setInitParameter("listings", "false")
        defaultServlet.setLoadOnStartup(1)
        defaultServlet.addMapping("*.html")
        defaultServlet.addMapping("*.css")
        defaultServlet.addMapping("*.js")
        defaultServlet.addMapping("*.png")

    }

}