package de.ddkfm.hcloud.models

data class FloatingIpActions(
        var id: Int,
        var command: String,
        var status: String,
        var progress: Int,
        var started: String,
        var finished: String?,
        var resources: ResourcesModel,
        var error: FloatingIpErrorModel
)

data class ResourcesModel(
        var id: Int,
        var type: String
)

data class FloatingIpErrorModel(
        var code: String,
        var message: String
)