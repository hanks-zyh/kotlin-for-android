package hanks.xyz.kotlinproject

import android.accounts.AccountManager
import android.app.*
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.app.usage.NetworkStatsManager
import android.app.usage.UsageStatsManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.content.RestrictionsManager
import android.content.pm.LauncherApps
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.fingerprint.FingerprintManager
import android.hardware.input.InputManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.media.midi.MidiManager
import android.media.projection.MediaProjectionManager
import android.media.session.MediaSessionManager
import android.media.tv.TvInputManager
import android.net.ConnectivityManager
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.nfc.NfcManager
import android.os.*
import android.os.storage.StorageManager
import android.print.PrintManager
import android.service.wallpaper.WallpaperService
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.NotificationCompat
import android.support.v7.widget.RecyclerView
import android.telecom.TelecomManager
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.inputmethod.InputMethodManager
import android.view.textservice.TextServicesManager
import android.widget.*

/**
 * Created by hanks on 2016/3/13.
 */

// 调用 toast 方法
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// 获取颜色
fun Context.color(res: Int): Int = ContextCompat.getColor(this, res) // 注意 getResource().getColor 已经不建议使用了

// 获取 LayoutInflater
fun Context.layoutInflater() = LayoutInflater.from(this)

fun Context.isConnected(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return manager.activeNetworkInfo?.isConnectedOrConnecting ?: true
}

// 网络错误
fun Activity.noNetworkSnackBar(parentView: View, retryListener: () -> Unit) {
    Snackbar.make(parentView, R.string.network_issue, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.try_again) {
                retryListener.invoke()
            }.show()
}


fun Int.dpToPx(context: Context) = this * context.resources.displayMetrics.density


fun RecyclerView.Adapter<RecyclerView.ViewHolder>.createHolder(view: ViewGroup) = object : RecyclerView.ViewHolder(view) {}
fun <T : View> RecyclerView.Adapter<RecyclerView.ViewHolder>.getView(holder: RecyclerView.ViewHolder) = holder.itemView as T

// View 可见性
fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun View.toggle(isVisible: Boolean) {
    if (isVisible) setVisible() else setGone()
}

fun View.setSize(width: Int, height: Int) {
    if (width <= 0 && height <= 0) return

    val layoutParams = when (this.layoutParams) {
        is LinearLayout.LayoutParams -> this.layoutParams as LinearLayout.LayoutParams
        is FrameLayout.LayoutParams -> this.layoutParams as FrameLayout.LayoutParams
        is RelativeLayout.LayoutParams -> this.layoutParams as RelativeLayout.LayoutParams
        else -> this.layoutParams
    }
    val ratio = height / width
    layoutParams.height = layoutParams.width * ratio
    this.layoutParams = layoutParams
}

// 弹出snackbar
fun View.snackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

fun View.snackbar(messageRes: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, messageRes, duration).show()
}

// 任意类打log
fun Any.log(message: String) {
    Log.e(this.javaClass.simpleName, message)
}


fun Context.getActivityManager() = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
fun Context.getAlarmManager() = getSystemService(Context.ALARM_SERVICE) as AlarmManager
fun Context.getAudioManager() = getSystemService(Context.AUDIO_SERVICE) as AudioManager
fun Context.getClipboardManager() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
fun Context.getConnectivityManager() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
fun Context.getKeyguardManager() = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
fun Context.getLayoutInflater() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
fun Context.getLocationManager() = getSystemService(Context.LOCATION_SERVICE) as LocationManager
fun Context.getNotificationManager() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
fun Context.getPowerManager() = getSystemService(Context.POWER_SERVICE) as PowerManager
fun Context.getSearchManager() = getSystemService(Context.SEARCH_SERVICE) as SearchManager
fun Context.getSensorManager() = getSystemService(Context.SENSOR_SERVICE) as SensorManager
fun Context.getTelephonyManager() = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
fun Context.getVibrator() = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
fun Context.getWallpaperService() = getSystemService(Context.WALLPAPER_SERVICE) as WallpaperService
fun Context.getWifiManager() = getSystemService(Context.WIFI_SERVICE) as WifiManager
fun Context.getWindowManager() = getSystemService(Context.WINDOW_SERVICE) as WindowManager
fun Context.getInputMethodManager() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
fun Context.getAccessibilityManager() = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
fun Context.getAccountManager() = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
fun Context.getDevicePolicyManager() = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
fun Context.getDropBoxManager() = getSystemService(Context.DROPBOX_SERVICE) as DropBoxManager
fun Context.getUiModeManager() = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
fun Context.getDownloadManager() = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
fun Context.getStorageManager() = getSystemService(Context.STORAGE_SERVICE) as StorageManager
fun Context.getNfcManager() = getSystemService(Context.NFC_SERVICE) as NfcManager
fun Context.getUsbManager() = getSystemService(Context.USB_SERVICE) as UsbManager
fun Context.getTextServicesManager() = getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE) as TextServicesManager
fun Context.getWifiP2pManager() = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
fun Context.getInputManager() = getSystemService(Context.INPUT_SERVICE) as InputManager
fun Context.getMediaRouter() = getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter
fun Context.getNsdManager() = getSystemService(Context.NSD_SERVICE) as NsdManager
fun Context.getDisplayManager() = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
fun Context.getUserManager() = getSystemService(Context.USER_SERVICE) as UserManager
fun Context.getBluetoothManager() = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
fun Context.getAppOpsManager() = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
fun Context.getCaptioningManager() = getSystemService(Context.CAPTIONING_SERVICE) as CaptioningManager
fun Context.getConsumerIrManager() = getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager
fun Context.getPrintManager() = getSystemService(Context.PRINT_SERVICE) as PrintManager
fun Context.getAppWidgetManager() = getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
fun Context.getBatteryManager() = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
fun Context.getCameraManager() = getSystemService(Context.CAMERA_SERVICE) as CameraManager
fun Context.getJobScheduler() = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
fun Context.getLauncherApps() = getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
fun Context.getMediaProjectionManager() = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
fun Context.getMediaSessionManager() = getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
fun Context.getRestrictionsManager() = getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager
fun Context.getTelecomManager() = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
fun Context.getTvInputManager() = getSystemService(Context.TV_INPUT_SERVICE) as TvInputManager
fun Context.getSubscriptionManager() = getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
fun Context.getUsageStatsManager() = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
fun Context.getCarrierConfigManager() = getSystemService(Context.CARRIER_CONFIG_SERVICE) as CarrierConfigManager
fun Context.getFingerprintManager() = getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
fun Context.getMidiManager() = getSystemService(Context.MIDI_SERVICE) as MidiManager
fun Context.getNetworkStatsManager() = getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager



// 扩展属性


val View.ctx: Context
    get() = context

var TextView.textColor: Int
    get() = currentTextColor
    set(v) = setTextColor(v)

inline fun Notification.build(context: Context, func: NotificationCompat.Builder.() -> Unit): Notification {
    val builder = NotificationCompat.Builder(context)
    builder.func()
    return builder.build()
}
