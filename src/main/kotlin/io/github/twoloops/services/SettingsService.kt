package io.github.twoloops.services

import io.github.twoloops.Application
import io.github.twoloops.dao.SettingsDaoImpl
import io.github.twoloops.helpers.Utils
import org.joda.time.DateTime

class SettingsService {
    var settingsDao = SettingsDaoImpl(Application.databaseConnection)

    fun setUpdated() {
        Utils.settings.lastSyncCourses = DateTime.now()
        settingsDao.update(Utils.settings.toPojo())
    }
}