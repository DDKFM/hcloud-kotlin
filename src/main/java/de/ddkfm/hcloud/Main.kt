package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest

fun main(args : Array<String>) {
    val token : String = "";
    var hCloud = KoHCloud(token = token);
    val servers = hCloud.getServers();
    for(server in servers) {
        println(server)
    }
}