package de.ddkfm.hcloud.models

import java.time.LocalDateTime

data class Image(
        var id: Int,
        var type: String,
        var status: String,
        var name: String,
        var description: String,
        var ImageSize: Double?,
        var DiskSize: Double,
        var created: LocalDateTime,
        var createdFrom: CreateFromData?,
        var BoundTo: Int?,
        var OsFlavor: String,
        var OsVersion: String,
        var RapidDeploy: Boolean
)

data class CreateFromData(
        var id: Int,
        var name: String
)