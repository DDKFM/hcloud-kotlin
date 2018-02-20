package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.CreateFromData
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.image
import org.json.JSONObject

class ImagesApi(token : String) : ApiBase(token = token) {

    // retrieve all available images from the api
    fun getImages(): List<image> {
        var url = "$endpoint/images";
        var req = this.get(url = "/images", header = null)
        val jsonResp = req?.asJson()?.body?.`object` ?: return emptyList();

        val Images = jsonResp.getJSONArray("servers");
        var returnList = mutableListOf<image>();

        Images.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonImages: JSONObject = it as JSONObject;
            var aImage = image(
                    id = jsonImages.getInt("id"),
                    type = jsonImages.getString("type"),
                    status = jsonImages.getString("status"),
                    name = jsonImages.getString("name"),
                    description = jsonImages.getString("description"),
                    ImageSize = jsonImages.getDouble("image_size"),
                    DiskSize = jsonImages.getDouble("disk_size"),
                    created = jsonImages.getString("created"),
                    createdFrom = CreateFromData(
                            id = jsonResp.getJSONObject("created_from").getInt("id"),
                            name = jsonResp.getJSONObject("created_from").getString("name")
                    ),
                    BoundTo = jsonImages.getInt("bound_to"),
                    OsFlavor = jsonImages.getString("os_flavor"),
                    OsVersion = jsonImages.getString("os_version"),
                    RapidDeploy = jsonImages.getBoolean("rapid_deploy")
            )

            returnList.add(aImage);
        }
        return returnList;
    }


}