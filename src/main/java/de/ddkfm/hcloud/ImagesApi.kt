package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import de.ddkfm.hcloud.models.Image
import org.json.JSONObject

class ImagesApi(token : String) : ApiBase(token = token) {

    // retrieve all available images from the api
    fun getImages(): List<Image> {
        var url = "$endpoint/images";
        var req = this.get(url = "/images", header = null)
        val jsonResp = req ?: return emptyList();

        val Images = jsonResp.getJSONArray("servers");
        var returnList = mutableListOf<Image>();

        Images.forEach {
            //Kotlin-Magic: "it" is automatically the current element in the JSONArray
            val jsonImage: JSONObject = it as JSONObject;
            var aImage = mapImage(jsonImage)

            returnList.add(aImage);
        }
        return returnList;
    }

    // get one specific image from ID
    fun getImage(id: Int): Image {
        var req = Unirest
                .get("$endpoint/images/" + id)
                .header("Authorization", auth);
        val jsonResp = req
                .asJson()
                .body
                .`object`;
        val jsonImage = jsonResp.getJSONObject("image");

        return mapImage(jsonImage)
    }
}