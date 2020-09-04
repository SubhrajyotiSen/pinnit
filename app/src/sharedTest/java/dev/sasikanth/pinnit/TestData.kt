package dev.sasikanth.pinnit

import dev.sasikanth.pinnit.data.PinnitNotification
import dev.sasikanth.pinnit.data.Schedule
import java.time.Instant
import java.util.UUID

object TestData {
  fun notification(
    uuid: UUID = UUID.randomUUID(),
    title: String = "Notification Title",
    content: String? = null,
    isPinned: Boolean = false,
    createdAt: Instant = Instant.now(),
    updatedAt: Instant = Instant.now(),
    deletedAt: Instant? = null,
    schedule: Schedule? = null
  ): PinnitNotification {
    return PinnitNotification(
      uuid = uuid,
      title = title,
      content = content,
      isPinned = isPinned,
      createdAt = createdAt,
      updatedAt = updatedAt,
      deletedAt = deletedAt,
      schedule = schedule
    )
  }
}
