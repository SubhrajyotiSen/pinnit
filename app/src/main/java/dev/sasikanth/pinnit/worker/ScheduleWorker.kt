package dev.sasikanth.pinnit.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dev.sasikanth.pinnit.data.PinnitNotification
import dev.sasikanth.pinnit.data.Schedule
import dev.sasikanth.pinnit.data.ScheduleType
import dev.sasikanth.pinnit.di.injector
import dev.sasikanth.pinnit.notifications.NotificationRepository
import dev.sasikanth.pinnit.utils.DispatcherProvider
import dev.sasikanth.pinnit.utils.UserClock
import dev.sasikanth.pinnit.utils.notification.NotificationUtil
import dev.sasikanth.pinnit.utils.toInstant
import dev.sasikanth.pinnit.utils.toLocalDateTime
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduleWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

  @Inject
  lateinit var repository: NotificationRepository

  @Inject
  lateinit var dispatcherProvider: DispatcherProvider

  @Inject
  lateinit var notificationUtil: NotificationUtil

  @Inject
  lateinit var userClock: UserClock

  companion object {
    const val INPUT_NOTIFICATION_UUID = "notification_uuid"

    fun scheduleNotificationRequest(
      notificationUuid: UUID,
      schedule: Schedule,
      userClock: UserClock
    ): WorkRequest {
      val currentInstant = Instant.now(userClock)
      val initialDelay = Duration.between(schedule.scheduledAt, currentInstant)

      val inputData = workDataOf(
        INPUT_NOTIFICATION_UUID to notificationUuid.toString()
      )

      return OneTimeWorkRequestBuilder<ScheduleWorker>()
        .setInputData(inputData)
        .setInitialDelay(initialDelay.toMillis(), TimeUnit.MILLISECONDS)
        .addTag("scheduled-notification-$notificationUuid")
        .build()
    }
  }

  init {
    injector.inject(this)
  }

  override suspend fun doWork(): Result {
    val notificationUuidString = inputData.getString(INPUT_NOTIFICATION_UUID)
    val notificationUuid = try {
      UUID.fromString(notificationUuidString)
    } catch (e: IllegalArgumentException) {
      return Result.failure()
    }

    withContext(dispatcherProvider.io) {
      // Getting notification from UUID
      val notification = repository.notification(notificationUuid)

      // If the notification is not already pinned, then pin the notification
      if (notification.isPinned.not()) {
        repository.toggleNotificationPinStatus(notification)
      }

      // Show notification (If it's already being shown, it will just refresh the notification)
      notificationUtil.showNotification(notification)

      // If there is no schedule or schedule type associated with schedule,
      // there is no point rescheduling it. So we will skip rescheduling it.
      if (notification.schedule?.scheduleType != null) {
        // Update scheduleAt to next scheduled time based on schedule type.
        reschedule(notification)
      }
    }
    return Result.success()
  }

  private suspend fun reschedule(notification: PinnitNotification) {
    val schedule = notification.schedule!!
    val scheduleType = schedule.scheduleType!!
    val userTimeZone = userClock.zone

    val scheduledAt = schedule.scheduledAt.toLocalDateTime(zoneId = userTimeZone)
    val updatedScheduledAt = when (scheduleType) {
      ScheduleType.Daily -> scheduledAt.plusDays(1L)
      ScheduleType.Weekly -> scheduledAt.plusWeeks(1L)
      ScheduleType.Monthly -> scheduledAt.plusMonths(1L)
    }

    val updatedNotification = notification.copy(
      schedule = schedule.copy(
        scheduledAt = updatedScheduledAt.toInstant(zoneId = userTimeZone)
      )
    )

    repository.updateNotification(updatedNotification)

    scheduleNotificationRequest(
      notificationUuid = updatedNotification.uuid,
      schedule = updatedNotification.schedule!!,
      userClock = userClock
    )
  }
}
