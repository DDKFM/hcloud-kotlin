package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest

fun main(args : Array<String>) {
    println("Hello World")
    val jsonResp = Unirest.get("https://jsonplaceholder.typicode.com/posts")
                          .asJson();
    println(jsonResp.body);
}