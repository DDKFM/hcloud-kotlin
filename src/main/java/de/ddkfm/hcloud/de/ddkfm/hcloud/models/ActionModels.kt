package de.ddkfm.hcloud.de.ddkfm.hcloud.models

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
        var id : Int,
        var command : String,
        var status : String,
        var progress : Int,
        var started : String,
        var finished : String,
        var resources : Resource,
        var error : Error
)