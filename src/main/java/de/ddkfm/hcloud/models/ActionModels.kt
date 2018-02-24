package de.ddkfm.hcloud.models

import java.time.LocalDateTime

/**
 * Created by maxsc on 20.02.2018.
 */

data class Error(
        var code : String,
        var message : String
)
data class Resource(
        var id : Int,
        var type : String
)
data class Action(
        var id: Int,
        var command: String,
        var status: ServerStatus,
        var progress: Int,
        var started: LocalDateTime,
        var finished: LocalDateTime,
        var resources: List<Resource>,
        var error: Error
)