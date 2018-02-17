package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import org.json.JSONObject

class KoHCloud {
    var auth : String = ""
    var endpoint : String = "https://api.hetzner.cloud/v1/";
    constructor(token : String) {
        this.auth = "Bearer $token";
    }

    fun getServers() : List<String> {

        val jsonResp = Unirest
                .get("$endpoint/servers")
                .header("Authorization", auth)
                .asJson()
                .body
                .`object`;
        val servers = jsonResp.getJSONArray("servers");
        var returnList = mutableListOf<String>();
        servers.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val server : JSONObject = it as JSONObject;
            returnList.add(server.getString("name"))
        }
        return returnList;
    }
}