package de.ddkfm.hcloud.de.ddkfm.hcloud.models

data class DataCenter(
        var id: Int,
        var name: String,
        var description: String,
        var recommendation: Int?
)

data class location(
        var  id : Int,
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

