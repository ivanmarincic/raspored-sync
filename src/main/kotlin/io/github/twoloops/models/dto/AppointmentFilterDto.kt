package io.github.twoloops.models.dto

import org.joda.time.DateTime

data class AppointmentFilterDto(
        var courseId: Int = -1,
        var lastSync: DateTime? = null,
        var partialCourseId: Int = -1,
        var partialStrings: List<String> = arrayListOf(),
        var blockedStrings: List<String> = arrayListOf()
)