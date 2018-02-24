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
        var server_typesRAW = jsonResp.getJSONArray("server_types");
        var returnList = mutableListOf<ServerType>();
        server_typesRAW.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonServerTypes: JSONObject = it as JSONObject;
            var server_types = ServerType(
                    id = jsonServerTypes.getInt("id"),
                    name = jsonServerTypes.getString("name"),
                    description = jsonResp.getString("description"),
                    cores = jsonServerTypes.getInt("cores"),
                    memory = jsonServerTypes.getInt("memory"),
                    disk = jsonServerTypes.getInt("disk"),
                    prices = Price(
                            location = jsonServerTypes.getJSONObject("prices")
                                    .getString("location"),
                            hourly = PriceEntry(
                                    net = jsonServerTypes.getJSONObject("prices")
                                            .getJSONObject("price_hourly")
                                            .getDouble("net"),
                                    gross = jsonServerTypes.getJSONObject("prices")
                                            .getJSONObject("price_hourly")
                                            .getDouble("gross")),
                            monthly = PriceEntry(
                                    net = jsonServerTypes.getJSONObject("prices")
                                            .getJSONObject("price_monthly")
                                            .getDouble("net"),
                                    gross = jsonServerTypes.getJSONObject("prices")
                                            .getJSONObject("price_monthly")
                                            .getDouble("gross")
                            )
                    ),
                    storageType = jsonServerTypes.getString("storage_type")

            );
            returnList.add(server_types);
        }
        return returnList;
    }

    // retrieve a specified server type
    fun getOneServerTypes(id: Int) : ServerType {
        var req = Unirest
                .get("$endpoint/server_types/" + id)
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        var jsonServer_Type = jsonResp.getJSONObject("server_type");
        var server_type = ServerType(
                id = jsonServer_Type.getInt("id"),
                name = jsonServer_Type.getString("name"),
                description = jsonResp.getString("description"),
                cores = jsonServer_Type.getInt("cores"),
                memory = jsonServer_Type.getInt("memory"),
                disk = jsonServer_Type.getInt("disk"),
                prices = Price(
                        location = jsonServer_Type.getJSONObject("prices")
                                .getString("location"),
                        hourly = PriceEntry(
                                net = jsonServer_Type.getJSONObject("prices")
                                        .getJSONObject("price_hourly")
                                        .getDouble("net"),
                                gross = jsonServer_Type.getJSONObject("prices")
                                        .getJSONObject("price_hourly")
                                        .getDouble("gross")),
                        monthly = PriceEntry(
                                net = jsonServer_Type.getJSONObject("prices")
                                        .getJSONObject("price_monthly")
                                        .getDouble("net"),
                                gross = jsonServer_Type.getJSONObject("prices")
                                        .getJSONObject("price_monthly")
                                        .getDouble("gross")
                        )
                ),
                storageType = jsonServer_Type.getString("storage_type")

        );
        return server_type;
    }
}