package de.ddkfm.hcloud.models

import java.util.*

data class price(
        var currency: String,
        var varRate: String,
        var image: priceInformation,
        var floatingIP: priceInformation,
        var traffic: priceInformation,
        var ServerBackup: String,
        var ServerTypes: ServerPrice
)

data class priceInformation(
        var net: String,
        var gross: String
)

data class ServerPrice(
        var id: Int,
        var name: String,
        var price: pricesServerModel
)

data class pricesServerModel(
        var location: String,
        var priceHour: priceInformation,
        var priceMonth: priceInformation
)