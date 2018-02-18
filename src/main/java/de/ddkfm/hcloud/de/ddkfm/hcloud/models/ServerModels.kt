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
        var id : Int,
        var name : String,
        var description : String,
        var cores : Int,
        var memory : Int,
        var disk : Int,
        var prices : Price,
        var storageType : String
)
data class Server(
        var id : Int,
        var name : String,
        var status : ServerStatus?,
        var created : LocalDateTime?,
        var publicNet : PublicNetwork?,
        var type : ServerType?,
        var datacenter : Any?,//TODO implement DataCenter later
        var image : Any?, //TODO implement Image later
        var iso : Any?, //TODO implement ISO later,
        var rescueEnabled : Boolean?,
        var locked : Boolean?,
        var backupWindow : String?,
        var outgoingTraffic : Int?,
        var incomingTraffic : Int?,
        var includedTraffic : Int?
)