package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import com.mashape.unirest.request.GetRequest
import com.mashape.unirest.request.body.RequestBodyEntity
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.*
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



class KoHCloud {
    var auth : String = ""
    var endpoint : String = "https://api.hetzner.cloud/v1";
    constructor(token : String) {
        this.auth = "Bearer $token";
    }

    private fun post(url : String, header : Map<String, String>?, json : JSONObject): RequestBodyEntity? {
        val headers = header ?: emptyMap();
        return Unirest
                .post("$endpoint$url")
                .headers(headers)
                .header("Authorization", auth)
                .body(json);
    }

    private fun put(url : String, header : Map<String, String>?, json : JSONObject): RequestBodyEntity? {
        val headers = header ?: emptyMap();
        return Unirest
                .put("$endpoint$url")
                .headers(headers)
                .header("Authorization", auth)
                .body(json);
    }

    private fun delete(url : String, header : Map<String, String>?, json : JSONObject): RequestBodyEntity? {
        val headers = header ?: emptyMap();
        return Unirest
                .delete("$endpoint$url")
                .headers(headers)
                .header("Authorization", auth)
                .body(json);
    }

    private fun get(url : String, header : Map<String, String>?): GetRequest? {
        val headers = header ?: emptyMap();
        return Unirest
                .get("$endpoint$url")
                .headers(headers)
                .header("Authorization", auth)
    }

    fun getServers() : List<Server> {
        var url = "$endpoint/servers";
        var req = this.get(url = "/servers", header = null)
        val jsonResp = req?.asJson()?.body?.`object` ?: return emptyList();

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

    //
    // data center branch
    //


    // retrieve all data centers
    fun getDataCenters() : List<DataCenter> {
        var url = "$endpoint/datacenters";
        var req = this.get(url = "/datacenters", header = null)
        val jsonResp = req?.asJson()?.body?.`object` ?: return emptyList();

        val datacenters = jsonResp.getJSONArray("datacenters");
        var returnList = mutableListOf<DataCenter>();
        datacenters.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonDataCenters : JSONObject = it as JSONObject;
            var dc = DataCenter(
                    id = jsonDataCenters.getInt("id"),
                    name = jsonDataCenters.getString("name"),
                    description = jsonDataCenters.getString("description")
            );
            returnList.add(dc);
        }
        return returnList;
    }

    // get one specific data center like fsn ngb
    fun getOneDataCenter(id: String): DataCenter {
        var url = "$endpoint/datacenters/"+id;
        var req = this.get(url = "/datacenters/"+id, header = null);

        val jsonResp = req.asJson().body.`object`;

        val jsondatacenter = jsonResp.getJSONObject("datacenter");

        var dc = DataCenter(
                id = jsondatacenter.getInt("id"),
                name = jsondatacenter.getString("name"),
                description = jsondatacenter.getString("description")
                );
        return dc;
    }


}