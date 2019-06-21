package io.github.twoloops.models.db

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import org.joda.time.DateTime

@DatabaseTable(tableName = "courses")
data class Course(
        @DatabaseField(columnName = "id", generatedId = true)
        var id: Int? = null,
        @DatabaseField(columnName = "name", unique = true)
        var name: String = "",
        @DatabaseField(columnName = "url")
        var url: String = "",
        @DatabaseField(columnName = "course_type_id", foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
        var type: CourseType = CourseType(),
        @DatabaseField(columnName = "year")
        var year: Int = -1,
        @DatabaseField(columnName = "last_sync", dataType = DataType.DATE_TIME)
        var lastSync: DateTime = DateTime(),
        @DatabaseField(columnName = "last_failed", dataType = DataType.DATE_TIME, canBeNull = true)
        var lastFailed: DateTime? = null
) {
    companion object {
        const val tableName = "courses"
    }
}