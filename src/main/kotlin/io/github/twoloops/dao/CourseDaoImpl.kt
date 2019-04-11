package io.github.twoloops.dao

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource
import io.github.twoloops.models.db.Course

class CourseDaoImpl(connectionSource: ConnectionSource) : BaseDaoImpl<Course, Int>(connectionSource, Course::class.java), CourseDao {

    override fun createIfNameNotExists(course: Course): Course {
        val obj = queryForEq("name", course.name).firstOrNull()
        return if (obj == null) {
            create(course)
            return queryForEq("name", course.name).first()
        } else {
            obj
        }
    }
}