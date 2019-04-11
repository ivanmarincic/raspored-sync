package io.github.twoloops.dao

import com.j256.ormlite.dao.Dao
import io.github.twoloops.models.db.Appointment
import io.github.twoloops.models.db.Course
import org.joda.time.DateTime

interface AppointmentDao : Dao<Appointment, Int> {
    fun getAllByDatesAndCourse(start: DateTime, end: DateTime, course: Course): List<Appointment>
    fun getNamesByCourseId(courseId: Int): List<String>
    fun getLatestByCourseId(courseId: Int, partialCourseId: Int, partialStrings: List<String>, blockedStrings: List<String>, start: DateTime, end: DateTime): List<Appointment>
    fun getByCourseIdAndName(courseId: Int, appointmentName: String): List<Appointment>
}