package de.ddkfm.hcloud

import com.mashape.unirest.request.body.RequestBodyEntity
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.*
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by maxsc on 18.02.2018.
 */
class ServerApi(token : String) : ApiBase(token = token) {

    fun getServers(name : String?) : List<Server?> {
        var url = "$endpoint/servers" + (if(name != null) "?name=$name" else "");
        var req = this.get(url = "/servers", header = null)
        val jsonResp = req?.asJson()?.body?.`object` ?: return emptyList();

        val servers = jsonResp.getJSONArray("servers");
        var returnList = mutableListOf<Server?>();
        servers.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonServer : JSONObject = it as JSONObject;
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            var server = this.mapServer(jsonServer);
            returnList.add(server)
        }
        return returnList;
    }

    fun getServer(id : Int) : Server? {
        var req = this.get("/servers/$id", null);
        var resp = req?.asJson()?.body?.`object` ?: return null;
        var jsonServer = resp.getJSONObject("server");

        return this.mapServer(jsonServer);
    }

    private fun mapServer(obj : JSONObject?) : Server? {
        if(obj == null)
            return null;
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return Server(
                id = obj.getInt("id"),
                name = obj.getString("name"),
                backupWindow = if (obj.has("backup_window"))
                    obj.get("backup_window")?.toString() ?: null
                else
                    null,
                created = LocalDateTime.parse(obj.getString("created"), formatter),
                datacenter = null,
                image = null,
                outgoingTraffic = obj.getInt("outgoing_traffic"),
                includedTraffic = obj.getInt("included_traffic"),
                incomingTraffic = obj.getInt("ingoing_traffic"),
                iso = null,
                locked = obj.getBoolean("locked"),
                publicNet = PublicNetwork(
                        ipv4 = IPv4(
                                ip = obj.getJSONObject("public_net")
                                        .getJSONObject("ipv4")
                                        .getString("ip"),
                                blocked = obj.getJSONObject("public_net")
                                        .getJSONObject("ipv4")
                                        .getBoolean("blocked"),
                                dnsPtr = obj.getJSONObject("public_net")
                                        .getJSONObject("ipv4")
                                        .getString("dns_ptr")
                        ),
                        ipv6 = IPv6(
                                ip = obj.getJSONObject("public_net")
                                        .getJSONObject("ipv6")
                                        .getString("ip"),
                                blocked = obj.getJSONObject("public_net")
                                        .getJSONObject("ipv6")
                                        .getBoolean("blocked"),
                                dnsPtr = obj.getJSONObject("public_net")
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
                rescueEnabled = obj.getBoolean("rescue_enabled"),
                status = ServerStatus.valueOf(obj.getString("status")),
                type = null
        );
    }
    fun changeServername(id : Int, name : String) : Server? {
        var resp = this.put("/servers/$id",
                        mapOf("Content-Type" to "application/json"),
                        JSONObject("{\"name\": \"$name\""))
        return this.mapServer(resp?.asJson()?.body?.`object` ?: null);
    }
    fun deleteServer(id : Int) : Action? {
        var resp = this.delete("/servers/$id", null, JSONObject());
        var json = resp?.asJson()?.body?.`object`?.getJSONObject("action") ?: return null;
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return Action(
                id = json.getInt("id"),
                command = json.getString("command"),
                status = ServerStatus.valueOf(json.getString("status")),
                progress =  json.getInt("progress"),
                started = LocalDateTime.parse(json.getString("started"), formatter),
                finished = LocalDateTime.parse(json.getString("finished"), formatter),
                resources = json.getJSONArray("resources")
                            .map { Resource(
                                    id = (it as JSONObject).getInt("id"),
                                    type = (it as JSONObject).getString("type")
                            )},
                error = Error(
                        code = json.getJSONObject("error").getString("code"),
                        message = json.getJSONObject("error").getString("message")
                )
        )
    }
}