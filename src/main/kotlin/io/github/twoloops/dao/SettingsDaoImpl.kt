package io.github.twoloops.dao

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource
import io.github.twoloops.models.db.Settings

class SettingsDaoImpl(connectionSource: ConnectionSource) : BaseDaoImpl<Settings, Int>(connectionSource, Settings::class.java), SettingsDao {
}