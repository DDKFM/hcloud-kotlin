package de.ddkfm.hcloud.de.ddkfm.hcloud.models

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.ApiBase
import org.json.JSONObject

class IsoApi(token : String) : ApiBase(token = token) {

    // retrieve all available ISOs
    fun getIsos() : List<isoModel> {
        var req = Unirest
                .get("$endpoint/isos")
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        var isoRAW = jsonResp.getJSONArray("isos");
        var returnList = mutableListOf<isoModel>();
        isoRAW.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonISO : JSONObject = it as JSONObject;
            var iso = isoModel(
                    id = jsonISO.getInt("id"),
                    name = jsonISO.getString("name"),
                    description = jsonISO.getString("description"),
                    type = jsonISO.getString("type")
            );
            returnList.add(iso);
        }
        return returnList;
    }
    // get one ISOs
    fun getOneIso(id: String): isoModel{
        var req = Unirest
                .get("$endpoint/isos/"+id)
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        var jsonISO = jsonResp.getJSONObject("iso");

        var iso = isoModel(
                id = jsonISO.getInt("id"),
                name = jsonISO.getString("name"),
                description = jsonISO.getString("description"),
                type = jsonISO.getString("type")
        );
        return iso;
    }
}