package de.ddkfm.hcloud.models

import com.mashape.unirest.http.HttpMethod
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.JsonNode
import com.mashape.unirest.request.GetRequest
import org.json.JSONObject

/**
 * Created by maxsc on 18.02.2018.
 */
class HCloudException(
    val code : String,
    override val message : String,
    val details : JSONObject
) : Exception() {

}

//fun GetRequest.asJSON