package dev.sasikanth.pinnit.notifications

import dev.sasikanth.pinnit.data.PinnitNotification
import dev.sasikanth.pinnit.di.AppScope
import dev.sasikanth.pinnit.utils.UtcClock
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.Instant
import java.util.UUID
import javax.inject.Inject

@AppScope
class NotificationRepository @Inject constructor(
  private val notificationDao: PinnitNotification.RoomDao,
  private val utcClock: UtcClock
) {

  suspend fun save(
    title: String,
    content: String? = null,
    isPinned: Boolean = false,
    uuid: UUID = UUID.randomUUID()
  ): PinnitNotification {
    val notification = PinnitNotification(
      uuid = uuid,
      title = title,
      content = content,
      isPinned = isPinned,
      createdAt = Instant.now(utcClock),
      updatedAt = Instant.now(utcClock),
      deletedAt = null
    )
    notificationDao.save(listOf(notification))
    return notification
  }

  suspend fun save(notifications: List<PinnitNotification>) {
    notificationDao.save(notifications)
  }

  suspend fun updateNotification(notification: PinnitNotification) {
    val updatedNotification = notification.copy(
      updatedAt = Instant.now(utcClock)
    )
    notificationDao.save(listOf(updatedNotification))
  }

  suspend fun notification(uuid: UUID): PinnitNotification {
    return notificationDao.notification(uuid)
  }

  suspend fun toggleNotificationPinStatus(notification: PinnitNotification) {
    val newPinStatus = !notification.isPinned
    notificationDao.updatePinStatus(notification.uuid, newPinStatus)
  }

  fun notifications(): Flow<List<PinnitNotification>> {
    return notificationDao.notifications()
  }
}