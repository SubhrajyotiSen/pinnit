package dev.sasikanth.pinnit.data

import android.os.Parcelable
import androidx.room.TypeConverter
import kotlinx.android.parcel.Parcelize
import java.time.Instant

@Parcelize
data class Schedule(
  val scheduledAt: Instant,
  val scheduleType: ScheduleType?
) : Parcelable

enum class ScheduleType {
  Daily, Weekly, Monthly
}

class ScheduleTypeConverter {
  @TypeConverter
  fun toScheduleType(value: String?): ScheduleType? {
    return if (value != null) {
      enumValueOf<ScheduleType>(value)
    } else {
      null
    }
  }

  @TypeConverter
  fun fromScheduleType(scheduleType: ScheduleType?): String? {
    return scheduleType?.toString()
  }
}
