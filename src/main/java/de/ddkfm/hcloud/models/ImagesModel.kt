package de.ddkfm.hcloud.models

data class image(
        var id: Int,
        var type: String,
        var status: String,
        var name: String,
        var description: String,
        var ImageSize: Double,
        var DiskSize: Double,
        var created: String,
        var createdFrom: CreateFromData,
        var BoundTo: Int,
        var OsFlavor: String,
        var OsVersion: String,
        var RapidDeploy: Boolean
)

data class CreateFromData(
        var id: Int,
        var name: String
)