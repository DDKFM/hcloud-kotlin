package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import java.io.File

fun main(args : Array<String>) {
    var filedata = File("./token.txt").readText();
    filedata = filedata.trim().replace("\\n", "").replace("\\t", "");
    val token : String = filedata;
    var hCloud = HCloudApi(token = token);
    val servers = hCloud.getServerApi().getServers();
    servers.forEach { println(it) }

    val locations = hCloud.getLocationApi().getLocations()
    locations.forEach { println(it)}
}