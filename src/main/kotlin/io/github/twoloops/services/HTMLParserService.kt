package io.github.twoloops.services

import io.github.twoloops.helpers.Utils
import io.github.twoloops.models.dto.AppointmentDto
import io.github.twoloops.models.dto.CourseDto
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.util.*


object HTMLParserService {

    fun parseRaspored(course: CourseDto, start: DateTime): ArrayList<AppointmentDto>? {
        val response = Jsoup
                .connect(course.url)
                .timeout(30000)
                .data(hashMapOf(
                        "StartDatee1" to start.toString("dd.MM.yyyy")
                ))
                .followRedirects(true)
                .execute()
        val document = response.parse()
        val weekTableColumns = document.select("#WeekTablee1 tbody tr")
        return if (weekTableColumns.size > 0) {
            val head = document.select("head")[0].select("script")
            getAppointments(head[head.size - 1].html(), response.cookies(), course)
        } else {
            null
        }
    }

    private fun getAppointments(js: String, cookies: Map<String, String>, course: CourseDto): ArrayList<AppointmentDto>? {
        val start = "FInAppointment = false;\r\n}\r\n}\r\n\r\n"
        val end = "\r\n\r\nvar PosFurtherUp"
        val indexOfStart = js.indexOf(start)
        val indexOfEnd = js.indexOf(end)
        if (indexOfStart == -1 || indexOfEnd == -1) {
            return null
        }
        val ssid = js.substringBefore(";").split("=")[1].replace("\"", "")
        val javaScript = js.substring(indexOfStart + start.length, indexOfEnd)
        val list = javaScript.split("\r\n\r\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val appointments = ArrayList<AppointmentDto>()
        for (item in list) {
            val itemSplits = item.split(";".toRegex())
            var guid: String = ""
            if (itemSplits.count() > 0) {
                guid = itemSplits[0].split("=")[1].split(",")[0].replace("'", "")
            }
            if (!guid.isBlank()) {
                val document = Jsoup
                        .connect(String.format(Utils.appointmentURL, ssid, guid))
                        .timeout(30000)
                        .cookies(cookies)
                        .get()
                appointments.add(parseElementForAppointment(document
                        .select("#AppInfoTable")
                        .select("tr")
                        .select(".h4"), course))
            }
        }
        return appointments
    }

    private fun parseElementForAppointment(elements: Elements, course: CourseDto): AppointmentDto {
        val appointment = AppointmentDto()
        for ((index, element) in elements.withIndex()) {
            when (index) {
                0 -> {
                    val text = element.text()
                    appointment.name = text.substringBefore("(").trim()
                    val detailsSplits = text.substringAfter("(").substringBeforeLast(")").split(",")
                    for (split in detailsSplits) {
                        if (split.matches(".*([D, ,P]\\d{3}|TCR).*".toRegex())) {
                            appointment.classroom = split.trim()
                        } else {
                            if (appointment.details.isBlank()) {
                                appointment.details = split.trim()
                            } else {
                                appointment.details += ", ${split.trim()}"
                            }
                        }
                    }
                }
                1 -> {
                    val text = element.text()
                    val lecturerSplits = text.substringAfter("(").substringBeforeLast(")").split(",")
                    if (lecturerSplits.count() > 1) {
                        for (split in lecturerSplits) {
                            if (!split.matches(".*\\d+.*".toRegex())) {
                                if (appointment.lecturer.isBlank()) {
                                    appointment.lecturer = split.trim()
                                } else {
                                    appointment.lecturer += ", ${split.trim()}"
                                }
                            }
                        }
                    }
                }
                2 -> {
                    val text = element.text()
                    val dates = text.split(" - ")
                    appointment.start = DateTime.parse(dates[0].trim(), DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")).withZone(DateTimeZone.UTC)
                    appointment.end = DateTime.parse(dates[1].trim(), DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")).withZone(DateTimeZone.UTC)
                }
            }
        }
        appointment.course = course
        return appointment
    }
}