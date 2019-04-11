package io.github.twoloops.models.dto

import io.github.twoloops.models.db.Settings
import org.joda.time.DateTime

data class SettingsDto(
        var id: Int = 1,
        var lastSyncCourses: DateTime = DateTime()
) {

    constructor(that: Settings) : this(that.id, that.lastSyncCourses)

    fun toPojo(): Settings {
        return Settings(id, lastSyncCourses)
    }
}