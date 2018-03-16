package de.ddkfm.hcloud

import de.ddkfm.hcloud.models.*
import org.json.JSONObject
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun mapServer(obj : JSONObject?) : Server? {
    if(obj == null)
        return null;
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    return Server(
            id = obj.getInt("id"),
            name = obj.getString("name"),
            backupWindow = if (obj.has("backup_window"))
                obj.get("backup_window")?.toString() ?: null
            else
                null,
            created = LocalDateTime.parse(obj.getString("created"), formatter),
            datacenter = mapDataCenter(obj.getJSONObject("datacenter")),
            image = mapImage(obj.getJSONObject("image")),
            outgoingTraffic = obj.getInt("outgoing_traffic"),
            includedTraffic = obj.getInt("included_traffic"),
            incomingTraffic = obj.getInt("ingoing_traffic"),
            iso = if(!obj.isNull("iso")) mapIso(obj.getJSONObject("iso")) else null,
            locked = obj.getBoolean("locked"),
            publicNet = PublicNetwork(
                    ipv4 = IPv4(
                            ip = obj.getJSONObject("public_net")
                                    .getJSONObject("ipv4")
                                    .getString("ip"),
                            blocked = obj.getJSONObject("public_net")
                                    .getJSONObject("ipv4")
                                    .getBoolean("blocked"),
                            dnsPtr = obj.getJSONObject("public_net")
                                    .getJSONObject("ipv4")
                                    .getString("dns_ptr")
                    ),
                    ipv6 = IPv6(
                            ip = obj.getJSONObject("public_net")
                                    .getJSONObject("ipv6")
                                    .getString("ip"),
                            blocked = obj.getJSONObject("public_net")
                                    .getJSONObject("ipv6")
                                    .getBoolean("blocked"),
                            dnsPtr = obj.getJSONObject("public_net")
                                    .getJSONObject("ipv6")
                                    .getJSONArray("dns_ptr")
                                    .map {
                                        val dnsPtrEntry = it as JSONObject
                                        IP(
                                                ip = dnsPtrEntry.getString("ip"),
                                                dnsPtr = dnsPtrEntry.getString("dns_ptr")
                                        )
                                    }


                    ),
                    floatingIPs = emptyList()
            ),
            rescueEnabled = obj.getBoolean("rescue_enabled"),
            status = ServerStatus.valueOf(obj.getString("status")),
            type = mapServerType(obj.getJSONObject("server_type"))
    );
}

fun mapImage(obj : JSONObject) : Image {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    return Image(
            id = obj.getInt("id"),
            name = obj.getString("name"),
            description = obj.getString("description"),
            BoundTo = if(!obj.isNull("bound_to"))
                obj.getInt("bound_to")
            else
                null,
            created = LocalDateTime.parse(obj.getString("created"), formatter),
            DiskSize = obj.getDouble("disk_size"),
            OsFlavor = obj.getString("os_flavor"),
            OsVersion = obj.getString("os_version"),
            status = obj.getString("status"),
            ImageSize = if(!obj.isNull("image_size"))
                obj.getDouble("image_size")
            else
                null,
            RapidDeploy = obj.getBoolean("rapid_deploy"),
            type = obj.getString("type"),
            createdFrom = if(!obj.isNull("created_from"))
                CreateFromData(
                        id = obj.getJSONObject("created_from").getInt("id"),
                        name = obj.getJSONObject("created_from").getString("name")
                )
            else
                null
    )
}
fun mapDataCenter(obj : JSONObject) : DataCenter {
    return DataCenter(
            id = obj.getInt("id"),
            name= obj.getString("name"),
            description = obj.getString("description"),
            location = mapLocation(obj.getJSONObject("location")),
            serverTypes = mapDataCenterServerType(obj.getJSONObject("server_types"))
    )
}

fun mapLocation(obj : JSONObject) : Location {
    return Location(
            id = obj.getInt("id"),
            name = obj.getString("name"),
            description = obj.getString("description"),
            city = obj.getString("city"),
            country = obj.getString("country"),
            latitude = obj.getDouble("latitude"),
            longitude = obj.getDouble("longitude")
    )
}
fun mapDataCenterServerType(obj : JSONObject) : DataCenterServerType {
    return DataCenterServerType(
            supported = obj.getJSONArray("supported").map { it as Int },
            available = obj.getJSONArray("available").map { it as Int }
    )
}

fun mapIso(jsonISO : JSONObject) : Iso {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    return Iso(
            id = jsonISO.getInt("id"),
            name = jsonISO.getString("name"),
            description = jsonISO.getString("description"),
            type = jsonISO.getString("type"),
            deprecated = LocalDateTime.parse(jsonISO.getString("deprecated"), formatter)
    );
}

public fun mapServerType(obj : JSONObject) : ServerType {
    var server_type = ServerType(
            id = obj.getInt("id"),
            name = obj.getString("name"),
            description = obj.getString("description"),
            cores = obj.getInt("cores"),
            memory = obj.getInt("memory"),
            disk = obj.getInt("disk"),
            prices = obj.getJSONArray("prices").map { mapPrices(it as JSONObject) },
            storageType = obj.getString("storage_type")
    );
    return server_type;
}
fun mapPrices(obj : JSONObject) : Price {
    var numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH)
    return Price(
            location = obj.getString("location"),
            hourly = PriceEntry(
                    net = numberFormat.parse(obj.getJSONObject("price_hourly").getString("net")).toDouble(),
                    gross = numberFormat.parse(obj.getJSONObject("price_hourly").getString("gross")).toDouble()
            ),
            monthly = PriceEntry(
                    net = numberFormat.parse(obj.getJSONObject("price_monthly").getString("net")).toDouble(),
                    gross = numberFormat.parse(obj.getJSONObject("price_monthly").getString("gross")).toDouble()
            )
    )
}