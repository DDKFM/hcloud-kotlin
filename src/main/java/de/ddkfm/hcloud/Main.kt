package de.ddkfm.hcloud

import de.ddkfm.hcloud.models.Metrics
import de.ddkfm.hcloud.models.Resource
import de.ddkfm.hcloud.models.Server
import org.apache.commons.logging.impl.Log4JLogger
import org.apache.log4j.*
import java.io.File
import java.time.LocalDateTime


fun main(args : Array<String>) {
    var filedata = File("./token_own.txt").readText();
    filedata = filedata.trim().replace("\\n", "").replace("\\t", "");
    val token : String = filedata;

    var test = Resource(id = 0, type = "")

    //Init Log4J
    val layout = PatternLayout("%r [%t] %p %c[%F:%L] %x - %m%n")
    var appender = ConsoleAppender(layout)
    appender.name = "ConsoleAppender"
    appender.threshold = Level.DEBUG
    appender.activateOptions()
    LogManager.getLogger("HCloud-Kotlin").addAppender(appender)

    var hCloud = HCloudApi(token);
    val servers = hCloud.getServerApi().getServers()
    println(servers)

    var server = hCloud.getServerApi().createServer("Dev-Test-02", "cx11",
            false, "ubuntu-16.04", emptyList(), "")

    var deleteAction = hCloud.getServerApi().deleteServer(server?.id!!)
    println(deleteAction)


}
