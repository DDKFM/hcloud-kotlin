package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.models.Price
import de.ddkfm.hcloud.models.PriceEntry
import de.ddkfm.hcloud.models.ServerType
import org.json.JSONObject

class ServerTypeApi(token : String) : ApiBase(token = token) {
    // retrieve all available server types
    fun getServerTypes() : List<ServerType> {
        var req = Unirest
                .get("$endpoint/server_types")
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        var servertypesRAW = jsonResp.getJSONArray("server_types");
        var returnList = mutableListOf<ServerType>();
        servertypesRAW.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonServerTypes: JSONObject = it as JSONObject;
            var server_types = mapServerType(jsonServerTypes);
            returnList.add(server_types);
        }
        return returnList;
    }

    // retrieve a specified server type
    fun getServerType(id: Int) : ServerType {
        var req = Unirest
                .get("$endpoint/server_types/" + id)
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        var jsonServer_Type = jsonResp.getJSONObject("server_type");

        return mapServerType(jsonServer_Type);
    }
}
