package com.example.sofascoreapp.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.CardColorEnum
import com.example.sofascoreapp.data.model.GoalTypeEnum
import com.example.sofascoreapp.data.model.Incident
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.databinding.MatchCardIncidentBinding
import com.example.sofascoreapp.databinding.MatchGoalIncidentHomeBinding
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
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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

    fun getLongDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObject = inputFormat.parse(date)
        val dayOfWeekFormat = SimpleDateFormat("EEE, dd.MM.yyyy.", Locale.getDefault())
        return dayOfWeekFormat.format(dateObject)
    }

    fun getDate(date: String): String? {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
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


    fun setImageGoal(imageView: ImageView, incident: Incident) {
        when (incident.goalType) {
            GoalTypeEnum.REGULAR -> imageView.setImageResource(R.drawable.icon_goal)
            GoalTypeEnum.PENALTY -> imageView.setImageResource(R.drawable.ic_penalty_score)
            else -> imageView.setImageResource(R.drawable.ic_autogoal)
        }
    }

    fun setImageCard(imageView: ImageView, incident: Incident) {
        when (incident.color) {
            CardColorEnum.YELLOW -> imageView.setImageResource(R.drawable.ic_yellow_card)
            else -> imageView.setImageResource(R.drawable.ic_red_card)
        }
    }

    fun toggleHomeAwayGoalLayout(
        binding: MatchGoalIncidentHomeBinding,
        incident: Incident,
        context: Context
    ) {
        binding.playerName.visibility = View.GONE
        binding.newResult.visibility = View.GONE
        binding.minute.visibility = View.GONE
        binding.goalIcon.visibility = View.GONE

        binding.playerNameAway.visibility = View.VISIBLE
        binding.newResultAway.visibility = View.VISIBLE
        binding.minuteAway.visibility = View.VISIBLE
        binding.goalIconAway.visibility = View.VISIBLE

        binding.minuteAway.text = context.getString(R.string.minute, incident.time)
        binding.minuteAway.text =
            context.getString(R.string.result, incident.homeScore, incident.awayScore)
        binding.playerNameAway.text = incident.player?.name
        setImageGoal(binding.goalIconAway, incident)
    }

    fun toggleHomeAwayCardLayout(
        binding: MatchCardIncidentBinding,
        incident: Incident,
        context: Context
    ) {
        binding.playerName.visibility = View.GONE
        binding.cardIcon.visibility = View.GONE
        binding.minute.visibility = View.GONE
        binding.reason.visibility = View.GONE

        binding.playerNameAway.visibility = View.VISIBLE
        binding.cardIconAway.visibility = View.VISIBLE
        binding.minuteAway.visibility = View.VISIBLE
        binding.reasonAway.visibility = View.VISIBLE

        binding.minuteAway.text = context.getString(R.string.minute, incident.time)
        binding.playerNameAway.text = incident.player?.name
        setImageCard(binding.cardIconAway, incident)
    }


}