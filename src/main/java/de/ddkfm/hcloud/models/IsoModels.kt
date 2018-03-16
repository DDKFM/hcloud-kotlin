package de.ddkfm.hcloud.models

import java.time.LocalDateTime

data class Iso (
        var id: Int,
        var name: String,
        var description: String,
        var type: String,
        var deprecated : LocalDateTime?
)