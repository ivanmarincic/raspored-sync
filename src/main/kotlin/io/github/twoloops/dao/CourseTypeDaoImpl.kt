package io.github.twoloops.dao

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource
import io.github.twoloops.models.jpa.CourseType

class CourseTypeDaoImpl(connectionSource: ConnectionSource) : BaseDaoImpl<CourseType, Int>(connectionSource, CourseType::class.java), CourseTypeDao {

    override fun createIfNameNotExists(courseType: CourseType): CourseType {
        val obj = queryForEq("name", courseType.name).firstOrNull()
        return if (obj == null) {
            create(courseType)
            return queryForEq("name", courseType.name).first()
        } else {
            obj
        }
    }
}