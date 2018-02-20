package de.ddkfm.hcloud.de.ddkfm.hcloud.models

import java.time.LocalDateTime

enum class ServerStatus{
    running,
    initializing,
    starting,
    stopping,
    off,
    deleting,
    migrating,
    rebuilding,
    unknown
}
data class IP(
    var ip : String,
    var dnsPtr: String
)

data class IPv4(
        var ip : String,
        var blocked : Boolean,
        var dnsPtr : String
)

data class IPv6(
        var ip : String,
        var blocked : Boolean,
        var dnsPtr : List<IP>?
)

data class PublicNetwork(
        var ipv4 : IPv4,
        var ipv6 : IPv6,
        var floatingIPs : List<Int>
)

data class PriceEntry(
        var net : Double,
        var gross : Double
)

data class Price(
        var location : String,
        var hourly : PriceEntry,
        var monthly : PriceEntry
)

data class ServerType(
        var id : Int? = null,
        var name : String? = null,
        var description : String? = null,
        var cores : Int? = null,
        var memory : Int? = null,
        var disk : Int? = null,
        var prices : Price? = null,
        var storageType : String? = null
)
data class Server(
        var id : Int? = null,
        var name : String? = null,
        var status : ServerStatus? = null,
        var created : LocalDateTime? = null,
        var publicNet : PublicNetwork? = null,
        var type : ServerType? = null,
        var datacenter : Any? = null,//TODO implement DataCenter later
        var image : Any? = null, //TODO implement Image later
        var iso : Any? = null, //TODO implement ISO later,
        var rescueEnabled : Boolean? = null,
        var locked : Boolean? = null,
        var backupWindow : String? = null,
        var outgoingTraffic : Int? = null,
        var incomingTraffic : Int? = null,
        var includedTraffic : Int? = null
)