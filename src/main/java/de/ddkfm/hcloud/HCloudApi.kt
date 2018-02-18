package de.ddkfm.hcloud

import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import com.mashape.unirest.request.GetRequest
import com.mashape.unirest.request.body.RequestBodyEntity
import de.ddkfm.hcloud.de.ddkfm.hcloud.models.*
import org.json.JSONObject
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



class HCloudApi(token: String) : ApiBase(token = token) {

    fun getDataCenterApi() : DataCenterApi = DataCenterApi(this.token)

    fun getFloatingIPApi() : FloatingIPApi = FloatingIPApi(this.token)

    fun getIsoApi() : IsoApi = IsoApi(this.token)

    fun getLocationApi() : LocationApi = LocationApi(this.token)

    fun getServerApi() : ServerApi = ServerApi(this.token)

    fun getServerTypeApi() : ServerTypeApi = ServerTypeApi(this.token)




}