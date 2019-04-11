package io.github.twoloops.dao

import com.j256.ormlite.dao.Dao
import io.github.twoloops.models.db.CourseType

interface CourseTypeDao : Dao<CourseType, Int>{
    fun createIfNameNotExists(courseType: CourseType): CourseType
}