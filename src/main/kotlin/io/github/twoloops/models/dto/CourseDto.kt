package io.github.twoloops.models.dto

import io.github.twoloops.models.db.Course
import org.joda.time.DateTime

data class CourseDto(
        var id: Int? = null,
        var name: String = "",
        var url: String = "",
        var type: CourseTypeDto = CourseTypeDto(),
        var year: Int = -1,
        var lastSync: DateTime = DateTime(),
        var lastFailed: DateTime? = null
) {

    constructor(that: Course) : this(that.id, that.name, that.url, CourseTypeDto(that.type), that.year, that.lastSync, that.lastFailed)

    fun toPojo(): Course {
        return Course(id, name, url, type.toPojo(), year, lastSync, lastFailed)
    }
}