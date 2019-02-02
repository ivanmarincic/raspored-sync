package io.github.twoloops.api

import io.github.twoloops.helpers.NotExpiredException
import io.github.twoloops.models.dto.CourseFilterDto
import io.github.twoloops.services.CourseService
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.apibuilder.ApiBuilder.path

object CourseController {

    val courseService = CourseService()

    fun initializeController(javalin: Javalin) {
        javalin.routes {
            path("/courses") {
                ApiBuilder.get("/getAll") {
                    it.json(courseService.getAll())
                }
                ApiBuilder.post("/getLatest") {
                    try {
                        it.json(courseService.getLatest(it.bodyAsClass(CourseFilterDto::class.java)))
                    } catch (e: NotExpiredException) {
                        it.status(204)
                    }
                }
            }
        }
    }
}