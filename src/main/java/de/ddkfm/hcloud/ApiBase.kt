package de.ddkfm.hcloud

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.JsonNode
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.request.GetRequest
import com.mashape.unirest.request.HttpRequest
import com.mashape.unirest.request.HttpRequestWithBody
import org.apache.log4j.LogManager
import org.json.JSONObject

/**
 * Created by maxsc on 18.02.2018.
 */

open class ApiBase {
    var token : String = ""
    var auth : String = ""
    protected val logger = LogManager.getLogger("HCloud-Kotlin")
    var endpoint : String = "https://api.hetzner.cloud/v1";
    constructor(token : String) {
        this.token = token;
        this.auth = "Bearer $token";
    }

    private fun request(type : String, url : String, header : Map<String, String>?, json : JSONObject? = null) : HttpResponse<JsonNode>? {
        val url = "$endpoint$url"
        var req : HttpRequest? =
                when(type.toLowerCase()) {
                    "get" -> Unirest.get(url)
                    "post" -> Unirest.post(url)
                    "put" -> Unirest.put(url)
                    "delete" -> Unirest.delete(url)
                    else -> null
               }
        var headers = header ?: emptyMap();
        var reqWithHeaders = req?.headers(headers)?.header("Authorization", auth) ?: return null
        var resp =
                try {
                    if (reqWithHeaders is GetRequest?) {
                        var getReq = reqWithHeaders as GetRequest?
                        getReq?.asJson();
                    } else
                        if (reqWithHeaders is HttpRequestWithBody?) {
                            var changeReq = reqWithHeaders as HttpRequestWithBody?
                            if (json != null) {
                                changeReq?.body(json)?.asJson()
                            } else
                                changeReq?.asJson()
                        } else
                            null
                } catch(e : Exception) {
                    logger.error("$type-Request failed: ", e)
                    null
                }
        when(resp?.status) {
            200, 201 -> {
                var gson = GsonBuilder().setPrettyPrinting().create()
                var parser = JsonParser()
                var formated = gson.toJson(parser.parse(resp?.body?.toString()))
                logger.debug("Resp: $formated")
                return resp
            }
            else -> {
                logger.error("$type-Request failed: [${resp?.status}: ${resp?.statusText}] Response: ${resp?.body.toString()}")
                return null
            }
        }
    }
    // POST request
    protected fun post(url : String, header : Map<String, String>?, json : JSONObject): JSONObject? {
        return this.request("POST", url, header, json)?.body?.`object`
    }
    // PUT request
    protected fun put(url : String, header : Map<String, String>?, json : JSONObject): JSONObject? {
        return this.request("PUT", url, header, json)?.body?.`object`
    }
    // DELETE request
    protected fun delete(url : String, header : Map<String, String>?, json : JSONObject): JSONObject? {
        return this.request("DELETE", url, header, json)?.body?.`object`
    }
    // GET request
    protected fun get(url : String, header : Map<String, String>?): JSONObject? {
        return this.request("GET", url, header)?.body?.`object`
    }
}
