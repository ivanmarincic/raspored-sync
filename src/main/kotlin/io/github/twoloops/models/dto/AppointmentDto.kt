package io.github.twoloops.models.dto

import io.github.twoloops.models.db.Appointment
import org.joda.time.DateTime

data class AppointmentDto(
        var id: Int? = null,
        var name: String = "",
        var course: CourseDto = CourseDto(),
        var details: String = "",
        var classroom: String = "",
        var lecturer: String = "",
        var start: DateTime = DateTime(),
        var end: DateTime = DateTime()
) {

    constructor(that: Appointment) : this(that.id, that.name, CourseDto(that.course), that.details, that.classroom, that.lecturer, that.start, that.end)

    fun toPojo(): Appointment {
        return Appointment(id, name, course.toPojo(), details, classroom, lecturer, start, end)
    }

    override fun equals(other: Any?): Boolean {
        if (other is AppointmentDto) {
            return name == other.name
                    && course == other.course
                    && details == other.details
                    && classroom == other.classroom
                    && lecturer == other.lecturer
                    && start == other.start
                    && end == other.end
        }
        return false
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: name.hashCode()
    }
}