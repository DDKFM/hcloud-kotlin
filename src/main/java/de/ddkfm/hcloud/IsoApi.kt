package de.ddkfm.hcloud.de.ddkfm.hcloud.models

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.ApiBase
import de.ddkfm.hcloud.mapIso
import de.ddkfm.hcloud.models.Iso
import org.json.JSONObject

class IsoApi(token : String) : ApiBase(token = token) {

    // retrieve all available ISOs
    fun getIsos() : List<Iso> {
        var req = Unirest
                .get("$endpoint/isos")
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        var isoRAW = jsonResp.getJSONArray("isos");
        var returnList = mutableListOf<Iso>();
        isoRAW.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonISO : JSONObject = it as JSONObject;
            returnList.add(mapIso(jsonISO));
        }
        return returnList;
    }
    // get one ISOs
    fun getIso(id: Int): Iso{
        var req = Unirest
                .get("$endpoint/isos/"+id)
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        var jsonISO = jsonResp.getJSONObject("iso");
        return return mapIso(jsonISO);
    }
}