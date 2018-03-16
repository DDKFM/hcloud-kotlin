package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.ApiBase
import de.ddkfm.hcloud.models.DataCenter
import org.json.JSONObject

class DataCenterApi(token : String) : ApiBase(token = token) {
    // retrieve all data centers
    fun getDataCenters() : List<DataCenter> {
        var req = Unirest
                .get("$endpoint/datacenters")
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        val datacenters = jsonResp.getJSONArray("datacenters");
        var returnList = mutableListOf<DataCenter>();
        datacenters.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonDataCenter : JSONObject = it as JSONObject;
            returnList.add(mapDataCenter(jsonDataCenter))
        }
        return returnList;
    }

    // get one specific data center like fsn ngb
    fun getOneDataCenter(id: Int): DataCenter {
        var req = Unirest
                .get("$endpoint/datacenters/" + id)
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        val jsondatacenter = jsonResp.getJSONObject("datacenter");

        return mapDataCenter(jsondatacenter)
    }

}
