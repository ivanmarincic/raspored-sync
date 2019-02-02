package io.github.twoloops.models.dto

import io.github.twoloops.models.jpa.Course
import org.joda.time.DateTime

data class CourseDto(
        var id: Int? = null,
        var name: String = "",
        var url: String = "",
        var type: CourseTypeDto = CourseTypeDto(),
        var year: Int = -1,
        var lastSync: DateTime = DateTime()
) {

    constructor(that: Course) : this(that.id, that.name, that.url, CourseTypeDto(that.type), that.year, that.lastSync)

    fun toPojo(): Course {
        return Course(id, name, url, type.toPojo(), year, lastSync)
    }
}