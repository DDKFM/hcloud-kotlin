package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import com.mashape.unirest.request.GetRequest
import com.mashape.unirest.request.body.RequestBodyEntity
import org.json.JSONObject

/**
 * Created by maxsc on 18.02.2018.
 */

open class ApiBase {
    var token : String = ""
    var auth : String = ""
    var endpoint : String = "https://api.hetzner.cloud/v1";
    constructor(token : String) {
        this.token = token;
        this.auth = "Bearer $token";
    }
    // POST request
    protected fun post(url : String, header : Map<String, String>?, json : JSONObject): RequestBodyEntity? {
        val headers = header ?: emptyMap();
        return try {
            Unirest
                    .post("$endpoint$url")
                    .headers(headers)
                    .header("Authorization", auth)
                    .body(json);
        } catch (e: Exception) {
            println("Error in POST-Methode with url: $url, header: $header, json: $json");
            null;
        }
    }
    // PUT request
    protected fun put(url : String, header : Map<String, String>?, json : JSONObject): RequestBodyEntity? {
        val headers = header ?: emptyMap();
        return try {
            Unirest
                    .put("$endpoint$url")
                    .headers(headers)
                    .header("Authorization", auth)
                    .body(json);
        } catch(e : Exception) {
            println("Error in PUT-Methode with url: $url, header: $header, json: $json");
            null;
        }
    }
    // DELETE request
    protected fun delete(url : String, header : Map<String, String>?, json : JSONObject): RequestBodyEntity? {
        val headers = header ?: emptyMap();
        return try {
            Unirest
                    .delete("$endpoint$url")
                    .headers(headers)
                    .header("Authorization", auth)
                    .body(json);
        } catch(e : Exception) {
            println("Error in DELETE-Methode with url: $url, header: $header, json: $json");
            null
        }
    }
    // GET request
    protected fun get(url : String, header : Map<String, String>?): GetRequest? {
        val headers = header ?: emptyMap();
        return try {
            Unirest
                    .get("$endpoint$url")
                    .headers(headers)
                    .header("Authorization", auth)
        } catch(e : Exception) {
            println("Error in GET-Methode with url: $url, header: $header");
            null
        }
    }
}
