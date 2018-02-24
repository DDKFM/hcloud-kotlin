package de.ddkfm.hcloud

import org.apache.commons.logging.impl.Log4JLogger
import org.apache.log4j.*
import java.io.File


fun main(args : Array<String>) {
    var filedata = File("./token_own.txt").readText();
    filedata = filedata.trim().replace("\\n", "").replace("\\t", "");
    val token : String = filedata;

    //Init Log4J
    val layout = PatternLayout("%r [%t] %p %c[%F:%L] %x - %m%n")
    var appender = ConsoleAppender(layout)
    appender.name = "ConsoleAppender"
    appender.threshold = Level.DEBUG
    appender.activateOptions()
    LogManager.getLogger("HCloud-Kotlin").addAppender(appender)

    var hCloud = HCloudApi(token);
    val servers = hCloud.getServerApi().getServer(505273)
    println(servers)
    val actions = hCloud.getActionsApi().getActions()
    println(actions)
}
