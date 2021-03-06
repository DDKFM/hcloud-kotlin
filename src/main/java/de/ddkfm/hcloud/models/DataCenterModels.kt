package de.ddkfm.hcloud.models

data class DataCenter(
        var id: Int,
        var name: String,
        var description: String,
        var location : Location,
        var serverTypes : DataCenterServerType
)
data class DataCenterServerType(
        var supported : List<Int>,
        var available : List<Int>
)

data class Location (
        var id : Int,
        var name : String,
        var description : String,
        var country : String,
        var city : String,
        var latitude : Double,
        var longitude : Double
)

data class server_types(
        var supported: List<Int>,
        var available: List<Int>
)
data class DCLocation(
        var id: Int,
        var name: String,
        var description: String,
        var country: String,
        var city: String,
        var latitude: Double,
        var longitude: Double
)

