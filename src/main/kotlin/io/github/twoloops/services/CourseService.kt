package io.github.twoloops.services

import io.github.twoloops.Application
import io.github.twoloops.dao.CourseDaoImpl
import io.github.twoloops.helpers.NotExpiredException
import io.github.twoloops.helpers.Utils
import io.github.twoloops.models.dto.CourseDto
import io.github.twoloops.models.dto.CourseFilterDto

class CourseService {

    val courseDao = CourseDaoImpl(Application.databaseConnection)

    fun getAll(): List<CourseDto> {
        return courseDao
                .queryBuilder()
                .orderBy("course_type_id", true)
                .orderBy("name", false)
                .query()
                .map { CourseDto(it) }
    }

    fun getLatest(courseFilter: CourseFilterDto): List<CourseDto> {
        if (courseFilter.lastSync == null || Utils.settings.lastSyncCourses > courseFilter.lastSync) {
            return courseDao.queryForAll().map { CourseDto(it) }
        } else {
            throw NotExpiredException()
        }
    }

    fun save(course: CourseDto): CourseDto {
        return CourseDto(courseDao.create(course.toPojo()))
    }
}