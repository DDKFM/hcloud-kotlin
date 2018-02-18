package de.ddkfm.hcloud.de.ddkfm.hcloud.models

import com.mashape.unirest.http.Unirest
import org.json.JSONObject

class dev_location {
    var auth : String = ""
    var endpoint : String = "https://api.hetzner.cloud/v1";
    constructor(token : String) {
        this.auth = "Bearer $token";
    }

    // retrieve all location
    fun getLocations() : List<DCLocation> {
        var req = Unirest
                .get("$endpoint/locations")
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        var location = jsonResp.getJSONArray("locations");
        var returnList = mutableListOf<DCLocation>();
        location.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonLocation : JSONObject = it as JSONObject;
            var loc = DCLocation(
                    id = jsonLocation.getInt("id"),
                    name = jsonLocation.getString("name"),
                    description = jsonLocation.getString("description"),
                    country = jsonLocation.getString("country"),
                    city = jsonLocation.getString("city"),
                    latitude = jsonLocation.getDouble("latitude"),
                    longitude = jsonLocation.getDouble("longitude")
            );
            returnList.add(loc);
        }
        return returnList;
    }

    // get one specific location like fsn ngb
    fun getOneLocation(id: String): DCLocation {
        var req = Unirest
                .get("$endpoint/locations/" + id)
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        val jsonLocation = jsonResp.getJSONObject("location");

        var loc = DCLocation(
                id = jsonLocation.getInt("id"),
                name = jsonLocation.getString("name"),
                description = jsonLocation.getString("description"),
                country = jsonLocation.getString("country"),
                city = jsonLocation.getString("city"),
                latitude = jsonLocation.getDouble("latitude"),
                longitude = jsonLocation.getDouble("longitude")
        );
        return loc;
    }

}
