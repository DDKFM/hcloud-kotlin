package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.DCLocation
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.FloatingIP
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.Server
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.dns
import org.json.JSONArray
import org.json.JSONObject

class FloatingIPApi(token : String) : ApiBase(token = token) {

    // retrieve all available floating IPs
    fun getFloatingIPs() : List<FloatingIP> {
        var req = Unirest
                .get("$endpoint/floating_ips")
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        var floating_ips_RAW = jsonResp.getJSONArray("floating_ips");
        var returnList = mutableListOf<FloatingIP>();
        floating_ips_RAW.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonFIps : JSONObject = it as JSONObject;
            var Ips = FloatingIP(
                    id = jsonFIps.getInt("id"),
                    description = jsonFIps.getString("description"),
                    ip = jsonFIps.getString("ip"),
                    type = jsonFIps.getString("type"),
                    server = jsonFIps.getInt("server"),
                    DnsPtr = dns(
                            ip = jsonFIps.getJSONObject("dns_ptr")
                                    .getString("ip"),
                            dns_ptr = jsonFIps.getJSONObject("dns_ptr")
                                    .getString("dns_ptr")
                    ),
                    HomeLocation = DCLocation(
                            id = jsonFIps.getJSONObject("home_location")
                                    .getInt("id"),
                            name = jsonFIps.getJSONObject("home_location")
                                    .getString("name"),
                            description = jsonFIps.getJSONObject("home_location")
                                    .getString("description"),
                            city = jsonFIps.getJSONObject("home_location")
                                    .getString("city"),
                            country = jsonFIps.getJSONObject("home_location")
                                    .getString("country"),
                            latitude = jsonFIps.getJSONObject("home_location")
                                    .getDouble("latitude"),
                            longitude = jsonFIps.getJSONObject("home_location")
                                    .getDouble("longitude")
                    ),
                    blocked = jsonFIps.getBoolean("blocked")
            );
            returnList.add(Ips);
        }
        return returnList;
    }

    // retrieve specific floating IP
    fun getSpecificFloatingIP(id: Int) : FloatingIP {
        var req = Unirest
                .get("$endpoint/floating_ips/"+id)
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        var jsonFIps = jsonResp.getJSONObject("floating_ip");
        var Ip = FloatingIP(
                id = jsonFIps.getInt("id"),
                description = jsonFIps.getString("description"),
                ip = jsonFIps.getString("ip"),
                type = jsonFIps.getString("type"),
                server = jsonFIps.getInt("server"),
                DnsPtr = dns(
                        ip = jsonFIps.getJSONObject("dns_ptr")
                                .getString("ip"),
                        dns_ptr = jsonFIps.getJSONObject("dns_ptr")
                                .getString("dns_ptr")
                ),
                HomeLocation = DCLocation(
                        id = jsonFIps.getJSONObject("home_location")
                                .getInt("id"),
                        name = jsonFIps.getJSONObject("home_location")
                                .getString("name"),
                        description = jsonFIps.getJSONObject("home_location")
                                .getString("description"),
                        city = jsonFIps.getJSONObject("home_location")
                                .getString("city"),
                        country = jsonFIps.getJSONObject("home_location")
                                .getString("country"),
                        latitude = jsonFIps.getJSONObject("home_location")
                                .getDouble("latitude"),
                        longitude = jsonFIps.getJSONObject("home_location")
                                .getDouble("longitude")
                ),
                blocked = jsonFIps.getBoolean("blocked")
        );
        return Ip;
    }

    // create a new floating ip address
    fun createFloatingIP(type : String?, homeLocation : String? = null, server : Int? = null, description: String? = null) : Boolean {
        var FloatingIPObj = JSONObject();
        FloatingIPObj.put("type", type);
        FloatingIPObj.put("home_location", homeLocation);
        FloatingIPObj.put("server", server);
        FloatingIPObj.put("description", description);
        var req = post(url = "/floating_ips", header = mapOf("Content-Type" to "application/json"), json = FloatingIPObj);
        req?.asJson();

        return true;
    }

    // update description of a floating IP
    fun changeDescriptionFloatingIp(id: Int, description: String): Boolean {
            var resp = this.put("/floating_ip/$id",
                    mapOf("Content-Type" to "application/json"),
                    JSONObject("{\"description\": \"$description\"}"))
            return true;
    }

    // delete a special floating ip
    fun deleteFloatingIps(id: Int) : Boolean {
        var req = Unirest
                .delete("$endpoint/floating_ips/" + id)
                .header("Authorization", auth);
        val response = req
                .asString()
                .body;
        return response != null && !response.isEmpty()
    }
}