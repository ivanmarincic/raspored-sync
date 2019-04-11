package io.github.twoloops.models.db

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "course_types")
data class CourseType(
        @DatabaseField(columnName = "id", generatedId = true)
        var id: Int? = null,
        @DatabaseField(columnName = "name", unique = true)
        var name: String = ""
) {
    companion object {
        const val tableName = "course_types"
    }
}