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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object PeriodicSyncTask {

    private val scheduledExecutorService = Executors.newScheduledThreadPool(1)
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var scheduledFuture: ScheduledFuture<*>
    private val coursesDao = CourseDaoImpl(Application.databaseConnection)
    private val appointmentDao = AppointmentDaoImpl(Application.databaseConnection)

    private val task = Runnable {
        try {
            Application.logger.info("Syncing courses")
            val courses = coursesDao.queryForAll().map { CourseDto(it) }
            val countDownLatch = CountDownLatch(courses.count())
            courses.forEach { course ->
                executor.submit {
                    var failed = 0
                    while (failed < Utils.syncTaskFailingThreshold) {
                        try {
                            processCourse(course)
                        } catch (e: Exception) {
                            Thread.sleep(TimeUnit.MINUTES.toMillis(5))
                            failed++
                        }
                    }
                    if (failed >= Utils.syncTaskFailingThreshold) {
                        if (course.lastFailed == null) {
                            course.lastFailed = DateTime.now()
                        }
                        coursesDao.update(course.toPojo())
                        Application.logger.error("Course " + course.name + " failed")
                    } else {
                        course.lastFailed = null
                        coursesDao.update(course.toPojo())
                    }
                    countDownLatch.countDown()
                }
            }
            countDownLatch.await()
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
        val newAppointments = arrayListOf<AppointmentDto>()
        if (syncedAppointments != null) {
            for (appointment in syncedAppointments) {
                val lastAppointmentIndex = lastAppointments.indexOf(appointment)
                if (lastAppointmentIndex != -1) {
                    lastAppointments.removeAt(lastAppointmentIndex)
                } else {
                    newAppointments.add(appointment)
                }
            }
            appointmentDao.deleteIds(lastAppointments.map {
                it.id
            })
            appointmentDao.create(newAppointments.map { it.toPojo() })
            if (newAppointments.count() > 0 || lastAppointments.count() > 0) {
                course.lastSync = DateTime.now()
                course.lastFailed = null
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
