package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import com.mashape.unirest.request.GetRequest
import com.mashape.unirest.request.body.RequestBodyEntity
import org.json.JSONObject

/**
 * Created by maxsc on 18.02.2018.
 */
// all base function are included here
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
        return Unirest
                .post("$endpoint$url")
                .headers(headers)
                .header("Authorization", auth)
                .body(json);
    }
    // PUT request
    protected fun put(url : String, header : Map<String, String>?, json : JSONObject): RequestBodyEntity? {
        val headers = header ?: emptyMap();
        return Unirest
                .put("$endpoint$url")
                .headers(headers)
                .header("Authorization", auth)
                .body(json);
    }
    // DELETE request
    protected fun delete(url : String, header : Map<String, String>?, json : JSONObject): RequestBodyEntity? {
        val headers = header ?: emptyMap();
        return Unirest
                .delete("$endpoint$url")
                .headers(headers)
                .header("Authorization", auth)
                .body(json);
    }
    // GET request
    protected fun get(url : String, header : Map<String, String>?): GetRequest? {
        val headers = header ?: emptyMap();
        var resp = Unirest
                .get("$endpoint$url")
                .headers(headers)
                .header("Authorization", auth)
        return resp;
    }
}
