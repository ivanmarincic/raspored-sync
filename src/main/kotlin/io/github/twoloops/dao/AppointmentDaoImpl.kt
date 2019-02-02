package io.github.twoloops.dao

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.dao.RawRowMapper
import com.j256.ormlite.support.ConnectionSource
import io.github.twoloops.models.jpa.Appointment
import io.github.twoloops.models.jpa.Course
import org.joda.time.DateTime


class AppointmentDaoImpl(connectionSource: ConnectionSource) : BaseDaoImpl<Appointment, Int>(connectionSource, Appointment::class.java), AppointmentDao {

    override fun getAllByDatesAndCourse(start: DateTime, end: DateTime, course: Course): List<Appointment> {
        return queryBuilder()
                .where()
                .ge("start", start)
                .and()
                .le("end", end)
                .and()
                .eq("course_id", course.id)
                .query()
    }

    override fun getNamesByCourseId(courseId: Int): List<String> {
        return queryRaw("select name from ${Appointment.tableName} where course_id = $courseId group by name",
                RawRowMapper { _, resultColumns -> resultColumns[0] })
                .results
    }

    override fun getLatestByCourseId(courseId: Int, partialCourseId: Int, partialStrings: List<String>, blockedStrings: List<String>, start: DateTime, end: DateTime): List<Appointment> {
        val partials = queryBuilder()
                .where()
                .eq("course_id", partialCourseId)
                .and()
                .`in`("name", partialStrings)
                .and()
                .ge("start", start)
                .and()
                .le("end", end)
                .and()
                .ne("course_id", courseId)
                .query()
        val appointments = queryBuilder()
                .where()
                .eq("course_id", courseId)
                .and()
                .notIn("name", blockedStrings)
                .and()
                .ge("start", start)
                .and()
                .le("end", end)
                .query()
        appointments.addAll(partials)
        appointments
                .sortBy {
                    it.start
                }
        return appointments
    }

    override fun getByCourseIdAndName(courseId: Int, appointmentName: String): List<Appointment> {
        return queryBuilder()
                .where()
                .eq("course_id", courseId)
                .and()
                .eq("name", appointmentName)
                .query()
    }
}