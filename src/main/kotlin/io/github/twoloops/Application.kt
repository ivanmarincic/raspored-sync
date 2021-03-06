package io.github.twoloops

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.j256.ormlite.db.MariaDbDatabaseType
import com.j256.ormlite.db.SqliteDatabaseType
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import io.github.twoloops.api.AppointmentController
import io.github.twoloops.api.CourseController
import io.github.twoloops.api.SettingsController
import io.github.twoloops.dao.CourseDaoImpl
import io.github.twoloops.dao.CourseTypeDaoImpl
import io.github.twoloops.dao.SettingsDaoImpl
import io.github.twoloops.helpers.Utils
import io.github.twoloops.models.db.Appointment
import io.github.twoloops.models.db.Course
import io.github.twoloops.models.db.CourseType
import io.github.twoloops.models.db.Settings
import io.github.twoloops.models.dto.SettingsDto
import io.github.twoloops.tasks.PeriodicSyncTask
import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import org.slf4j.LoggerFactory

class Application {

    companion object {
        val databaseConnection: ConnectionSource = JdbcPooledConnectionSource("jdbc:sqlite:${Utils.databaseFile.absoluteFile}", SqliteDatabaseType())
        val logger = LoggerFactory.getLogger(Application::class.java)!!
    }
}

fun main() {
    initializeDatabase()
    val app = Javalin
            .create()
            .enableCaseSensitiveUrls()
            .start(5001)
    JavalinJackson
            .configure(ObjectMapper()
                    .registerModule(JodaModule())
                    .registerModule(KotlinModule()))
    CourseController.initializeController(app)
    AppointmentController.initializeController(app)
    SettingsController.initializeController(app)
    PeriodicSyncTask.start()
    Application.logger.info("Server has started and is ready to go")
}

fun initializeDatabase() {
    TableUtils.createTableIfNotExists(Application.databaseConnection, Course::class.java)
    TableUtils.createTableIfNotExists(Application.databaseConnection, CourseType::class.java)
    TableUtils.createTableIfNotExists(Application.databaseConnection, Appointment::class.java)
    TableUtils.createTableIfNotExists(Application.databaseConnection, Settings::class.java)

    val settingsDao = SettingsDaoImpl(Application.databaseConnection)
    val courseTypeDao = CourseTypeDaoImpl(Application.databaseConnection)
    val courseDao = CourseDaoImpl(Application.databaseConnection)

    Utils.settings = SettingsDto(settingsDao.createIfNotExists(Settings()))

    val racunarstvo = courseTypeDao.createIfNameNotExists(CourseType(name = Utils.courseTypeRacunarstvo))
    val strojarstvo = courseTypeDao.createIfNameNotExists(CourseType(name = Utils.courseTypeStrojarstvo))
    val elektrotehnika = courseTypeDao.createIfNameNotExists(CourseType(name = Utils.courseTypeElektrotehnika))

    Utils.racunarstvoPreddiplomskiURLs.forEach { (url, course) ->
        courseDao.createIfNameNotExists(Course(name = course, url = url, type = racunarstvo, year = Utils.extractYear(course)))
    }

    Utils.racunarstvoDiplomskiURLs.forEach { (url, course) ->
        courseDao.createIfNameNotExists(Course(name = course, url = url, type = racunarstvo, year = Utils.extractYear(course)))
    }

    Utils.strojarstvoPreddiplomskiURLs.forEach { (url, course) ->
        courseDao.createIfNameNotExists(Course(name = course, url = url, type = strojarstvo, year = Utils.extractYear(course)))
    }

    Utils.strojarstvoDiplomskiURLs.forEach { (url, course) ->
        courseDao.createIfNameNotExists(Course(name = course, url = url, type = strojarstvo, year = Utils.extractYear(course)))
    }

    Utils.elektrotehnikaPreddiplomskiURLs.forEach { (url, course) ->
        courseDao.createIfNameNotExists(Course(name = course, url = url, type = elektrotehnika, year = Utils.extractYear(course)))
    }

    Utils.elektrotehnikaDiplomskiURLs.forEach { (url, course) ->
        courseDao.createIfNameNotExists(Course(name = course, url = url, type = elektrotehnika, year = Utils.extractYear(course)))
    }
    Application.logger.info("Database initialized")
}
