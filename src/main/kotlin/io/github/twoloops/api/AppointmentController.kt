package io.github.twoloops.api

import io.github.twoloops.helpers.NotExpiredException
import io.github.twoloops.models.dto.AppointmentFilterDto
import io.github.twoloops.services.AppointmentService
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder

object AppointmentController {

    val appointmentService = AppointmentService()

    fun initializeController(javalin: Javalin) {
        javalin.routes {
            ApiBuilder.path("/appointments") {
                ApiBuilder.get("/getAll") {
                    it.json(appointmentService.getAll())
                }

                ApiBuilder.get("/getByCourse/:courseId") {
                    it.json(appointmentService.getNamesByCourseId(it.pathParam("courseId").toInt()))
                }

                ApiBuilder.post("/getLatest") {
                    try {
                        it.json(appointmentService.getLatest(it.bodyAsClass(AppointmentFilterDto::class.java)))
                    } catch (e: NotExpiredException) {
                        it.status(204)
                    }
                }

                ApiBuilder.get("/getLatest/:courseId") {
                    try {
                        it.json(appointmentService.getLatest(AppointmentFilterDto(courseId = it.pathParam("courseId").toInt())))
                    } catch (e: NotExpiredException) {
                        it.status(204)
                    }
                }

                ApiBuilder.get("/getByCourseAndName/:courseId/:appointmentName") {
                    it.json(appointmentService.getByCourseIdAndName(it.pathParam("courseId").toInt(), it.pathParam("appointmentName")))
                }
            }
        }
    }
}