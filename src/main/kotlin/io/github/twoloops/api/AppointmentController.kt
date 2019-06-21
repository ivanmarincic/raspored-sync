package io.github.twoloops.api

import io.github.twoloops.api.exceptions.NotExpiredException
import io.github.twoloops.models.dto.AppointmentDto
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
                        val data = appointmentService.getLatest(it.bodyAsClass(AppointmentFilterDto::class.java))
                        if (data.outOfSync) {
                            it.status(211)
                        }
                        it.json(data.appointments)
                    } catch (e: NotExpiredException) {
                        it.json(emptyArray<AppointmentDto>()).status(210)
                    }
                }

                ApiBuilder.get("/getLatest/:courseId") {
                    try {
                        val data = appointmentService.getLatest(AppointmentFilterDto(courseId = it.pathParam("courseId").toInt()))
                        if (data.outOfSync) {
                            it.status(211)
                        }
                        it.json(data.appointments)
                    } catch (e: NotExpiredException) {
                        it.status(210)
                    }
                }

                ApiBuilder.get("/getByCourseAndName/:courseId/:appointmentName") {
                    it.json(appointmentService.getByCourseIdAndName(it.pathParam("courseId").toInt(), it.pathParam("appointmentName")))
                }
            }
        }
    }
}
