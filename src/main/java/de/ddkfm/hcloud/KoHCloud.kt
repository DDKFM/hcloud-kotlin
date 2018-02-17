package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.Server
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.ServerStatus
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.ServerType
import org.json.JSONObject
import java.time.LocalDateTime

class KoHCloud {
    var auth : String = ""
    var endpoint : String = "https://api.hetzner.cloud/v1";
    constructor(token : String) {
        this.auth = "Bearer $token";
    }

    fun getServers() : List<Server> {

        var url = "$endpoint/servers";
        var req = Unirest
                .get("$endpoint/servers")
                .header("Authorization", auth);
        val jsonResp = req
                    .asJson()
                    .body
                    .`object`;
        val servers = jsonResp.getJSONArray("servers");
        var returnList = mutableListOf<Server>();
        servers.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonServer : JSONObject = it as JSONObject;
            var server = Server(
                    id = jsonServer.getInt("id"),
                    name = jsonServer.getString("name"),
                    backupWindow = "",
                    created = LocalDateTime.now(),
                    datacenter = null,
                    image = null,
                    includedTraffic = 0,
                    incomingTraffic = 0,
                    iso = null,
                    locked = false,
                    outgoingTraffic = 0,
                    publicNet = null,
                    rescueEnabled = false,
                    status = ServerStatus.valueOf(jsonServer.getString("status")),
                    type = null
            );
            returnList.add(server)
        }
        return returnList;
    }
}