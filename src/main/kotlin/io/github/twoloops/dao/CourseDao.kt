package io.github.twoloops.dao

import com.j256.ormlite.dao.Dao
import io.github.twoloops.models.db.Course

interface CourseDao : Dao<Course, Int> {
    fun createIfNameNotExists(course: Course): Course
}