package io.github.twoloops.models.dto

import io.github.twoloops.models.jpa.Settings
import org.joda.time.DateTime

data class SettingsDto(
        var id: Int = 1,
        var lastSyncCourses: DateTime = DateTime()
) {

    constructor(that: Settings) : this(that.id, that.lastSyncCourses)

    fun toPojo(): SettingsDto {
        return SettingsDto(id, lastSyncCourses)
    }
}