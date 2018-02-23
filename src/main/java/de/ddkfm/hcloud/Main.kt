package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.Server
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.ServerType
import java.io.File

fun main(args : Array<String>) {
    //var filedata = File("./token.txt").readText();
    //filedata = filedata.trim().replace("\\n", "").replace("\\t", "");
    //val token : String = filedata;
    var hCloud = HCloudApi(token = "");
    //var server = hCloud.getServerApi().createServer("kotlinDevServer", "cx11", false, "ubuntu-16.04", emptyList(), "");
    //if(server != null)
    //    hCloud.getServerApi().deleteServer(server.id!!);
    //println(server)

    hCloud.getFloatingIPApi().createFloatingIP("ipv4");
}
