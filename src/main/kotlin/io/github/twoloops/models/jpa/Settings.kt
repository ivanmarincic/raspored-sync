package io.github.twoloops.models.jpa

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import org.joda.time.DateTime

@DatabaseTable(tableName = "settings")
data class Settings(
        @DatabaseField(columnName = "id", id = true)
        var id: Int = 1,
        @DatabaseField(columnName = "last_sync_courses", dataType = DataType.DATE_TIME)
        var lastSyncCourses: DateTime = DateTime()
) {
    companion object {
        const val tableName = "settings"
    }
}