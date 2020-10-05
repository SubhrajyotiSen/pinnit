package dev.sasikanth.pinnit.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset.UTC

fun Instant.toLocalDateTime(zoneId: ZoneId = UTC): LocalDateTime {
  return atZone(zoneId).toLocalDateTime()
}

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun LocalDateTime.toInstant(zoneId: ZoneId = UTC): Instant {
  return atZone(zoneId).toInstant()
}
