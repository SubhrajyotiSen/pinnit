package dev.sasikanth.pinnit.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifs")
data class PinnitItem(
    @PrimaryKey
    @ColumnInfo(name = "notif_key")
    val notifKey: Long,
    @ColumnInfo(name = "notif_id")
    val notifId: Int,
    @ColumnInfo(name = "notif_icon")
    val notifIcon: Uri? = null,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "text")
    val content: String = "",
    @ColumnInfo(name = "messages")
    val messages: List<Message> = emptyList(),
    @ColumnInfo(name = "package_name")
    val packageName: String,
    @ColumnInfo(name = "app_label")
    val appLabel: String,
    @ColumnInfo(name = "posted_on")
    val postedOn: Long,
    @ColumnInfo(name = "template")
    val template: TemplateStyle = TemplateStyle.DefaultStyle,
    @ColumnInfo(name = "is_pinned")
    val isPinned: Boolean = false,
    @ColumnInfo(name = "is_current")
    val isCurrent: Boolean = false
) {

  fun removeFromCurrent(): PinnitItem =
      copy(isCurrent = false)

  fun contentHashCode(): Int {
    var result = notifKey.hashCode()
    result = 31 * result + notifId
    result = 31 * result + notifIcon.hashCode()
    result = 31 * result + title.hashCode()
    result = 31 * result + content.hashCode()
    result = 31 * result + messages.hashCode()
    result = 31 * result + packageName.hashCode()
    result = 31 * result + appLabel.hashCode()
    result = 31 * result + postedOn.hashCode()
    result = 31 * result + template.hashCode()
    return result
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as PinnitItem

    if (notifKey != other.notifKey) return false
    if (notifId != other.notifId) return false
    if (notifIcon != other.notifIcon) return false
    if (title != other.title) return false
    if (content != other.content) return false
    if (messages != other.messages) return false
    if (packageName != other.packageName) return false
    if (appLabel != other.appLabel) return false
    if (postedOn != other.postedOn) return false
    if (template != other.template) return false
    if (isPinned != other.isPinned) return false
    if (isCurrent != other.isCurrent) return false

    return true
  }

  fun equalsLastItem(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as PinnitItem

    if (notifKey != other.notifKey) return false
    if (notifId != other.notifId) return false
    if (notifIcon != other.notifIcon) return false
    if (title != other.title) return false
    if (content != other.content) return false
    if (messages != other.messages) return false
    if (packageName != other.packageName) return false
    if (appLabel != other.appLabel) return false
    if (template != other.template) return false
    if (isPinned != other.isPinned) return false
    if (isCurrent != other.isCurrent) return false

    return true
  }

  override fun hashCode(): Int {
    var result = notifKey.hashCode()
    result = 31 * result + notifId
    result = 31 * result + notifIcon.hashCode()
    result = 31 * result + title.hashCode()
    result = 31 * result + content.hashCode()
    result = 31 * result + messages.hashCode()
    result = 31 * result + packageName.hashCode()
    result = 31 * result + appLabel.hashCode()
    result = 31 * result + postedOn.hashCode()
    result = 31 * result + template.hashCode()
    result = 31 * result + isPinned.hashCode()
    result = 31 * result + isCurrent.hashCode()
    return result
  }
}