package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest

class KoHCloud {
    var auth : String = ""
    constructor(token : String) {
        this.auth = "Bearer $token";
    }

    fun getServers() : List<String> {

        val jsonResp = Unirest
                .get("https://api.hetzner.cloud/v1/servers")
                .header("Authorization", auth)
                .asJson()
                .body
                .`object`;
        val servers = jsonResp.getJSONArray("servers");
        var returnList = mutableListOf<String>();
        val length = servers.length()
        for(i in 0 until servers.length()) {
            returnList.add(servers.getJSONObject(i).getString("name"))
        }
        return returnList;
    }
}