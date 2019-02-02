package io.github.twoloops.dao

import com.j256.ormlite.dao.Dao
import io.github.twoloops.models.jpa.Settings

interface SettingsDao : Dao<Settings, Int> {
}