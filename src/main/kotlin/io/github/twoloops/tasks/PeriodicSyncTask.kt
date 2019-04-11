package io.github.twoloops.tasks

import io.github.twoloops.Application
import io.github.twoloops.dao.AppointmentDaoImpl
import io.github.twoloops.dao.CourseDaoImpl
import io.github.twoloops.helpers.Utils
import io.github.twoloops.models.dto.AppointmentDto
import io.github.twoloops.models.dto.CourseDto
import io.github.twoloops.services.HTMLParserService
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object PeriodicSyncTask {

    private val scheduledExecutorService = Executors.newScheduledThreadPool(1)!!
    private lateinit var scheduledFuture: ScheduledFuture<*>
    private val coursesDao = CourseDaoImpl(Application.databaseConnection)
    private val appointmentDao = AppointmentDaoImpl(Application.databaseConnection)

    private val task = Runnable {
        try {
            Application.logger.info("Syncing courses")
            val courses = coursesDao.queryForAll().map { CourseDto(it) }
            courses.forEach { course ->
                var failed = 0
                var exception: Exception? = null
                while (failed < Utils.syncTaskFailingTreshold) {
                    try {
                        processCourse(course)
                    } catch (e: Exception) {
                        failed++
                        exception = e
                    }
                }
                if (failed >= Utils.syncTaskFailingTreshold) {
                    Application.logger.error("Course " + course.name + " failed", exception)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            Application.logger.info("Courses syncing finished")
        }
    }

    private fun processCourse(course: CourseDto, nextWeek: Boolean = false) {
        var now = DateTime()
        if (nextWeek) {
            now = now.plusDays(DateTimeConstants.DAYS_PER_WEEK)
        }
        val weekStart = now.withDayOfWeek(DateTimeConstants.MONDAY).withTimeAtStartOfDay()
        val weekEnd = weekStart.plusDays(DateTimeConstants.DAYS_PER_WEEK)
        val lastAppointments = appointmentDao.getAllByDatesAndCourse(weekStart, weekEnd, course.toPojo()).asSequence().map { AppointmentDto(it) }.toMutableList()
        val syncedAppointments = HTMLParserService.parseRaspored(course, weekStart)
        if (syncedAppointments != null) {
            for (i in (syncedAppointments.count() - 1)..0) {
                val appointment = syncedAppointments[i]
                val lastAppointmentIndex = lastAppointments.indexOf(appointment)
                if (lastAppointmentIndex != -1) {
                    lastAppointments.removeAt(lastAppointmentIndex)
                    syncedAppointments.removeAt(i)
                }
            }
            appointmentDao.deleteIds(lastAppointments.map {
                it.id
            })
            appointmentDao.create(syncedAppointments.map { it.toPojo() })
            if (syncedAppointments.count() > 0) {
                course.lastSync = DateTime.now()
                coursesDao.update(course.toPojo())
            }
        }
        if (!nextWeek) {
            processCourse(course, true)
        }
    }

    fun start(time: Long = Utils.syncTaskExecutionTimeMinutes) {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(task, 0, time, TimeUnit.MINUTES)
    }
}