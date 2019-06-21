package io.github.twoloops.models.dto

data class AppointmentSyncDto(
        val appointments: List<AppointmentDto>,
        val outOfSync: Boolean
)