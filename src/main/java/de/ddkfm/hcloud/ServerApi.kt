package de.ddkfm.hcloud

import de.ddkfm.hcloud.models.*
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.time.*
import java.time.format.DateTimeFormatter

/**
 * Created by maxsc on 18.02.2018.
 */
class ServerApi(token : String) : ApiBase(token = token) {

    // retrieve all server of a project
    fun getServers(name : String? = null) : List<Server?> {
        var url = "$endpoint/servers" + (if(name != null) "?name=$name" else "");
        var jsonResp = this.get(url = "/servers", header = null) ?: return emptyList();

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
    // retrieve a specified server from id
    fun getServer(id : Int) : Server? {
        var resp = this.get("/servers/$id", null) ?: return null;
        var jsonServer = resp.getJSONObject("server");
        return this.mapServer(jsonServer);
    }

    // map for data return of the server modules
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
    //change attributes of a specified server from id
    fun changeServername(id : Int, name : String) : Server? {
        var resp = this.put("/servers/$id",
                        mapOf("Content-Type" to "application/json"),
                        JSONObject("{\"name\": \"$name\"}"))
        return this.mapServer(resp?.getJSONObject("server") ?: null);
    }

    // delete the specified server
    fun deleteServer(id : Int) : Action? {
        var resp = delete("/servers/$id", mapOf("Content-Type" to "application/json"), JSONObject());
        var json = resp?.getJSONObject("action") ?: return null;
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
                error = if(json.isNull("error"))
                            null
                        else
                            Error(
                                code = json.getJSONObject("error").getString("code"),
                                message = json.getJSONObject("error").getString("message")
                            )
        )
    }
    // create a new server
    fun createServer(name : String, type : String, startAfterCreate : Boolean, image: String/*TODO: get image from serverobject and not from a parameter*/, sshKeys : List<Int>, userData: String) : Server? {
        var serverObj = JSONObject();
        serverObj.put("name", name)
        serverObj.put("server_type", type)
        serverObj.put("start_after_create", startAfterCreate)
        serverObj.put("image", image)
        serverObj.put("ssh_keys", JSONArray(sshKeys))
        serverObj.put("user_data", userData)
        var resp = post(url = "/servers", header = mapOf("Content-Type" to "application/json"), json = serverObj)
        return mapServer(resp?.getJSONObject("server")) ?: return null;
    }

    fun getMetrics(id : Int, type : List<String>, start : LocalDateTime, end : LocalDateTime, step : Int? = null) : Metrics? {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        var typeString = type.joinToString(separator = ",")
        var formatedStart = start.format(formatter)
        var formatedEnd = end.format(formatter)
        var formatedStep = "&step=$step" ?: ""
        var url = "/servers/$id/metrics?type=$typeString&start=$formatedStart&end=$formatedEnd$step"
        var resp = this.get(url = url, header = null);
        var jsonMetrics = resp?.getJSONObject("metrics") ?: return null
        var metrics = Metrics(
                start = LocalDateTime.parse(jsonMetrics.getString("start"), formatter),
                end = LocalDateTime.parse(jsonMetrics.getString("end"), formatter),
                step = jsonMetrics.getInt("step"),
                timeSeries = mutableListOf()
        )
        var jsonTimeSeries = jsonMetrics.getJSONObject("time_series");
        for(key in jsonTimeSeries.keySet()) {
            var jsonMetric = jsonTimeSeries.getJSONObject(key);
            var metric = TimeSeries(
                    name = key,
                    values = mutableListOf()
            )
            var jsonValues = jsonMetric.getJSONArray("values")
            for(jsonValue in jsonValues) {
                if(jsonValue is JSONArray) {
                    val longValue = jsonValue.getLong(0) * 1000;
                    val time = LocalDateTime.ofInstant(Instant.ofEpochMilli(longValue), ZoneId.systemDefault());
                    val value = BigDecimal(jsonValue.getString(1))
                    metric.values.add(MetricsData(
                            time = time,
                            value = value
                    ))
                }
            }
            metrics.timeSeries.add(metric)
        }
        return metrics

    }
}