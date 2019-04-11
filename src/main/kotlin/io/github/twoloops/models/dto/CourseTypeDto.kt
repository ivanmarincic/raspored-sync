package io.github.twoloops.models.dto

import io.github.twoloops.models.db.CourseType

data class CourseTypeDto(
        var id: Int? = null,
        var name: String = ""
) {

    constructor(that: CourseType) : this(that.id, that.name)

    fun toPojo(): CourseType {
        return CourseType(id, name)
    }
}