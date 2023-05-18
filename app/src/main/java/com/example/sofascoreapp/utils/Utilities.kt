package com.example.sofascoreapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.sofascoreapp.MainActivity
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.CardColorEnum
import com.example.sofascoreapp.data.model.GoalTypeEnum
import com.example.sofascoreapp.data.model.Incident
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.databinding.MatchCardIncidentBinding
import com.example.sofascoreapp.databinding.MatchGoalIncidentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.Period
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class Utilities {

    fun getMatchHour(dateTime: String): String {
        val zonedDateTime = ZonedDateTime.parse(dateTime)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return formatter.format(zonedDateTime)
    }

    fun getTodaysDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }

    fun getDayInWeek(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateObject = inputFormat.parse(date)
        val dayOfWeekFormat = SimpleDateFormat("EEE", Locale.getDefault())
        return dayOfWeekFormat.format(dateObject).toUpperCase()
    }

    fun getAvailableDateShort(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObject = inputFormat.parse(date)
        val dayOfWeekFormat = SimpleDateFormat("dd.MM.", Locale.getDefault())
        return dayOfWeekFormat.format(dateObject).toUpperCase()
    }

    fun getInvertedAvailableDateShort(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObject = inputFormat.parse(date)
        val dayOfWeekFormat = SimpleDateFormat("MM.dd.", Locale.getDefault())
        return dayOfWeekFormat.format(dateObject).toUpperCase()
    }

    fun getLongDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObject = inputFormat.parse(date)
        val dayOfWeekFormat = SimpleDateFormat("EEE, dd.MM.yyyy.", Locale.getDefault())
        return dayOfWeekFormat.format(dateObject)
    }

    fun getInvertedLongDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObject = inputFormat.parse(date)
        val dayOfWeekFormat = SimpleDateFormat("EEE, MM.dd.yyyy.", Locale.getDefault())
        return dayOfWeekFormat.format(dateObject)
    }

    fun getDate(date: String): String? {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val offsetDateTime = OffsetDateTime.parse(date)
        return offsetDateTime.format(formatter)
    }

    fun getInvertedDate(date: String): String? {
        val formatter = DateTimeFormatter.ofPattern("MM.dd.yyyy")
        val offsetDateTime = OffsetDateTime.parse(date)
        return offsetDateTime.format(formatter)
    }

    fun calculateForignPlayers(playersNat: List<String>, clubCountry: String): Int {
        var count = 0
        for (player in playersNat) {
            if (player != clubCountry) {
                count++
            }
        }
        return count
    }

    fun isTomorrow(dateTimeString: String): Boolean {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val dateTime = OffsetDateTime.parse(dateTimeString, formatter)
        val currentDateTime = LocalDateTime.now(ZoneOffset.UTC).plusDays(1)
        return dateTime.toLocalDate() == currentDateTime.toLocalDate()
    }

    fun isToday(dateTimeString: String): Boolean {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val dateTime = OffsetDateTime.parse(dateTimeString, formatter)
        val currentDateTime = LocalDateTime.now(ZoneOffset.UTC)
        return dateTime.toLocalDate() == currentDateTime.toLocalDate()
    }

    fun getDateOfBirth(dateTimeString: String): String {
        val inputFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val dateTime = LocalDate.parse(dateTimeString, inputFormat)
        val outputFormat = DateTimeFormatter.ofPattern("d MMM yyyy")
        return dateTime.format(outputFormat)
    }

    fun getInvertedDateOfBirth(dateTimeString: String): String {
        val inputFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val dateTime = LocalDate.parse(dateTimeString, inputFormat)
        val outputFormat = DateTimeFormatter.ofPattern("MMM d yyyy")
        return dateTime.format(outputFormat)
    }

    fun calculateYears(dateTimeString: String): Int {
        val inputFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val dateTime = LocalDate.parse(dateTimeString, inputFormat)
        val currentDate = LocalDate.now()
        val period = Period.between(dateTime, currentDate)
        return period.years
    }

    fun makeTeamMembersList(coach: Player, players: ArrayList<Player>): ArrayList<Any> {
        val teamMembersList = ArrayList<Any>()
        teamMembersList.add("Coach")
        teamMembersList.add(coach)
        teamMembersList.add("Players")
        teamMembersList.addAll(players)
        return teamMembersList
    }

    fun restartApp(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun setRotatingText(textView: TextView) {
        textView.isSelected = true
        textView.isSingleLine = true
        textView.marqueeRepeatLimit = -1
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val conManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = conManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    companion object {
        private var isNoInternetDialogShown = false

        fun showNoInternetDialog(context: Context, method: () -> Unit) {
            if (!isNoInternetDialogShown) {

                isNoInternetDialogShown = true

                Handler(Looper.getMainLooper()).post {
                    MaterialAlertDialogBuilder(context)
                        .setTitle(context.getString(R.string.no_internet_title))
                        .setMessage(context.getString(R.string.no_internet_description))
                        .setPositiveButton(context.getString(R.string.retry)) { _, _ ->
                            isNoInternetDialogShown = false
                            GlobalScope.launch {
                                delay(2000)
                                method.invoke()
                            }
                        }
                        .show()
                }
            }
        }

        fun TextView.clear() {
            this.text = ""
        }

        fun ImageView.clear() {
            this.setImageDrawable(null)
        }

    }

    fun setWinningTint(context: Context, vararg texts: TextView) {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(
            R.attr.on_surface_on_surface_lv_1,
            typedValue,
            true
        )
        val color = ContextCompat.getColor(context, typedValue.resourceId)

        for (item in texts) {
            item.setTextColor(color)
        }
    }


}