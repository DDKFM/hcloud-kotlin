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
        var latitude : String,
        var longitude : String
)

data class server_types(
        var supported: List<Int>,
        var available: List<Int>
)


