package de.ddkfm.hcloud

import de.ddkfm.hcloud.models.Metrics
import org.apache.commons.logging.impl.Log4JLogger
import org.apache.log4j.*
import java.io.File
import java.time.LocalDateTime


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
    val servers = hCloud.getServerApi().getServers()
    println(servers)

    var metrics = hCloud.getServerApi().getMetrics(
            id = 505273,
            type = listOf("cpu", "network", "disk"),
            start = LocalDateTime.now().minusDays(5),
            end = LocalDateTime.now()
    )
    println(metrics)

    /*
    hCloud.getServerApi().createServer(
            name = "test",
            type = "cx11",
            startAfterCreate = false,
            image = "ubuntu-16.04",
            sshKeys = emptyList(),
            userData = "")
    */

}
