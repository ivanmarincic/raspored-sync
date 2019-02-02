package io.github.twoloops.services

import io.github.twoloops.Application
import io.github.twoloops.dao.AppointmentDaoImpl
import io.github.twoloops.dao.CourseDaoImpl
import io.github.twoloops.helpers.NotExpiredException
import io.github.twoloops.models.dto.AppointmentDto
import io.github.twoloops.models.dto.AppointmentFilterDto
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants

class AppointmentService {

    private val appointmentDao = AppointmentDaoImpl(Application.databaseConnection)
    private val courseDao = CourseDaoImpl(Application.databaseConnection)

    fun getAll(): List<AppointmentDto> {
        return appointmentDao.queryForAll().map { AppointmentDto(it) }
    }

    fun getNamesByCourseId(courseId: Int): List<String> {
        return appointmentDao.getNamesByCourseId(courseId)
    }

    fun getLatest(appointmentFilterDto: AppointmentFilterDto): List<AppointmentDto> {
        val course = courseDao.queryForId(appointmentFilterDto.courseId)
        if (appointmentFilterDto.lastSync == null || course.lastSync > appointmentFilterDto.lastSync) {
            val start = DateTime().withDayOfWeek(DateTimeConstants.MONDAY).withTimeAtStartOfDay()
            val end = start.plusDays(DateTimeConstants.DAYS_PER_WEEK * 2)
            return appointmentDao.getLatestByCourseId(appointmentFilterDto.courseId, appointmentFilterDto.partialCourseId, appointmentFilterDto.partialStrings, appointmentFilterDto.blockedStrings, start, end).map { AppointmentDto(it) }
        } else {
            throw NotExpiredException()
        }
    }

    fun getByCourseIdAndName(courseId: Int, appointmentName: String): List<AppointmentDto> {
        return appointmentDao.getByCourseIdAndName(courseId, appointmentName).map { AppointmentDto(it) }
    }
}