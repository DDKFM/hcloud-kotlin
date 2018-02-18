package de.ddkfm.hcloud.de.ddkfm.hcloud.models

data class FloatingIP(
        var id: Int,
        var description: String,
        var ip: String,
        var type: String,
        var server: Int,
        var DnsPtr: dns,
        var HomeLocation: DCLocation,
        var blocked: Boolean
)

data class dns(
        var ip: String,
        var dns_ptr: String
)