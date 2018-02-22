package de.ddkfm.hcloud.de.ddkfm.hcloud.models

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.ApiBase
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
    fun getOneDataCenter(id: Int): DataCenter {
        var req = Unirest
                .get("$endpoint/datacenters/" + id)
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        val jsondatacenter = jsonResp.getJSONObject("datacenter");

        var dc = DataCenter(
                id = jsondatacenter.getInt("id"),
                name = jsondatacenter.getString("name"),
                description = jsondatacenter.getString("description")
        );
        return dc;
    }

}
