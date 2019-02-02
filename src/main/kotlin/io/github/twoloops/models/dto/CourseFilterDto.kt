package io.github.twoloops.models.dto

import org.joda.time.DateTime

data class CourseFilterDto(
        var lastSync: DateTime? = null
)