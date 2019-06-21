package io.github.twoloops.models.db

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import org.joda.time.DateTime

@DatabaseTable(tableName = "appointments")
data class Appointment(
        @DatabaseField(columnName = "id", generatedId = true)
        var id: Int? = null,
        @DatabaseField(columnName = "name")
        var name: String = "",
        @DatabaseField(columnName = "course_id", foreign = true, foreignAutoRefresh = true)
        var course: Course = Course(),
        @DatabaseField(columnName = "details")
        var details: String = "",
        @DatabaseField(columnName = "classroom")
        var classroom: String = "",
        @DatabaseField(columnName = "lecturer")
        var lecturer: String = "",
        @DatabaseField(columnName = "start_time", dataType = DataType.DATE_TIME)
        var start: DateTime = DateTime(),
        @DatabaseField(columnName = "end_time", dataType = DataType.DATE_TIME)
        var end: DateTime = DateTime()
) {
    companion object {
        const val tableName = "appointments"
    }
}