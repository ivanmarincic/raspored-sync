package io.github.twoloops.api

import io.github.twoloops.helpers.Utils
import io.github.twoloops.services.SettingsService
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.apibuilder.ApiBuilder.path

object SettingsController {

    val settingsService = SettingsService()

    fun initializeController(javalin: Javalin) {
        javalin.routes {
            path("/settings") {
                ApiBuilder.get("/setUpdated/:key") {
                    if (it.pathParam("key") == Utils.uniqueUpdateKey) {
                        settingsService.setUpdated()
                    }
                }
            }
        }
    }
}