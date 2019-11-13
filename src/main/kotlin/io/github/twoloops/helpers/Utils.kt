package io.github.twoloops.helpers

import io.github.twoloops.models.dto.SettingsDto
import java.io.File

class Utils {
    companion object {

        lateinit var settings: SettingsDto

        val databaseFile = File("${System.getProperty("user.home")}/raspored-sync-db/raspored-sync.db")
        val appointmentURL = "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/internal/calendar.appointmentinfo?OQS=%s&guid=%s&tablename=appointment&hideusers=no"

        const val syncTaskFailingTreshold = 5
        const val syncTaskExecutionTimeMinutes = 30L
        const val failedSyncTaskExecutionTimeMinutes = 5L

        const val uniqueUpdateKey = "7RE3TG8M"

        const val courseTypeStrojarstvo = "Strojarstvo"
        const val courseTypeRacunarstvo = "Računarstvo"
        const val courseTypeElektrotehnika = "Elektrotehnika"

        fun extractYear(course: String): Int {
            return try {
                Integer.valueOf(course.substringBefore("godina").substringAfter("studij").substringBefore(".").replace(" ", ""))
            } catch (e: Exception) {
                -1
            }
        }

        val racunarstvoPreddiplomskiURLs = hashMapOf(
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar1/calendar?" to "Računarstvo preddiplomski studij 1. godina",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar2/calendar?" to "Računarstvo preddiplomski studij 2. godina",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar3/calendar?" to "Računarstvo preddiplomski studij 3. godina"
        )

        val racunarstvoDiplomskiURLs = hashMapOf(
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar7/calendar?" to "Računarstvo diplomski studij 1. godina - PIIS",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar4/calendar?" to "Računarstvo diplomski studij 1. godina - RSM",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar8/calendar?" to "Računarstvo diplomski studij 2. godina - PIIS",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar5/calendar?" to "Računarstvo diplomski studij 2. godina - RSM"
        )

        val strojarstvoPreddiplomskiURLs = hashMapOf(
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar6/calendar?" to "Strojarstvo preddiplomski studij 1. godina",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar16/calendar?" to "Strojarstvo preddiplomski studij 2. godina",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar17/calendar?" to "Strojarstvo preddiplomski studij 3. godina - KIRP",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar18/calendar?" to "Strojarstvo preddiplomski studij 3. godina - PI",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar20/calendar?" to "Strojarstvo preddiplomski studij 3. godina - M"
        )

        val strojarstvoDiplomskiURLs = hashMapOf(
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar21/calendar?" to "Strojarstvo diplomski studij 1. godina - KIRP",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar22/calendar?" to "Strojarstvo diplomski studij 1. godina - PI",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar24/calendar?" to "Strojarstvo diplomski studij 1. godina - M",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar25/calendar?" to "Strojarstvo diplomski studij 2. godina - KIRP",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar26/calendar?" to "Strojarstvo diplomski studij 2. godina - PI",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar28/calendar?" to "Strojarstvo diplomski studij 2. godina - M"
        )

        val elektrotehnikaPreddiplomskiURLs = hashMapOf(
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar44/calendar?" to "Elektrotehnika preddiplomski studij 1. godina",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar44/calendar1?" to "Elektrotehnika preddiplomski studij 2. godina",
                "http://intranet.fsr.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar44/calendar2?" to "Elektrotehnika preddiplomski studij 3. godina"
        )

        val elektrotehnikaDiplomskiURLs = hashMapOf(
                "http://intranet.fsre.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar46/calendar" to "Elektrotehnika diplomski studij 1. godina AiSU",
                "http://intranet.fsre.sum.ba:81/intranetfsr/teamworks.dll/calendar/calendar46/calendar2" to "Elektrotehnika diplomski studij 1. godina EE"
        )
    }
}
