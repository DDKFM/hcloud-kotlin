package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.*
import org.json.JSONObject
import java.time.LocalDateTime
import javax.swing.JFrame
import java.time.format.DateTimeFormatter



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
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            var server = Server(
                    id = jsonServer.getInt("id"),
                    name = jsonServer.getString("name"),
                    backupWindow = if (jsonServer.has("backup_window"))
                            jsonServer.get("backup_window")?.toString() ?: null
                        else
                            null,
                    created = LocalDateTime.parse(jsonServer.getString("created"), formatter),
                    datacenter = null,
                    image = null,
                    outgoingTraffic = jsonServer.getInt("outgoing_traffic"),
                    includedTraffic = jsonServer.getInt("included_traffic"),
                    incomingTraffic = jsonServer.getInt("ingoing_traffic"),
                    iso = null,
                    locked = jsonServer.getBoolean("locked"),
                    publicNet = PublicNetwork(
                            ipv4 = IPv4(
                                    ip = jsonServer.getJSONObject("public_net")
                                            .getJSONObject("ipv4")
                                            .getString("ip"),
                                    blocked = jsonServer.getJSONObject("public_net")
                                            .getJSONObject("ipv4")
                                            .getBoolean("blocked"),
                                    dnsPtr = jsonServer.getJSONObject("public_net")
                                            .getJSONObject("ipv4")
                                            .getString("dns_ptr")
                            ),
                            ipv6 = IPv6(
                                    ip = jsonServer.getJSONObject("public_net")
                                            .getJSONObject("ipv6")
                                            .getString("ip"),
                                    blocked = jsonServer.getJSONObject("public_net")
                                            .getJSONObject("ipv6")
                                            .getBoolean("blocked"),
                                    dnsPtr = jsonServer.getJSONObject("public_net")
                                            .getJSONObject("ipv6")
                                            .getJSONArray("dns_ptr")
                                            .map {
                                                val dnsPtrEntry = it as JSONObject
                                                IP(
                                                  ip = dnsPtrEntry.getString("ip"),
                                                  dnsPtr = dnsPtrEntry.getString("dns_ptr")
                                                )
                                            }


                            ),
                            floatingIPs = emptyList()
                    ),
                    rescueEnabled = jsonServer.getBoolean("rescue_enabled"),
                    status = ServerStatus.valueOf(jsonServer.getString("status")),
                    type = null
            );
            returnList.add(server)
        }
        return returnList;
    }
}