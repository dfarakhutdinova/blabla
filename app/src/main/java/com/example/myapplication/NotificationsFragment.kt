package com.example.myapplication

import android.app.*
import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat.getLongDateFormat
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.getSystemService
import androidx.core.view.get
import com.example.myapplication.databinding.FragmentNotificationsBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text
import java.text.DateFormat
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


class NotificationsFragment : Fragment(R.layout.fragment_notifications) {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotificationsBinding.bind(view)

        createNotificationChannel()
        binding.notificationButton.setOnClickListener {scheduleNotification()}
    }

    private fun scheduleNotification() {
        val intent = Intent(requireContext().applicationContext, Notification::class.java)
        val title = requireView().findViewById<TextInputEditText>(R.id.titleET).text.toString()


        intent.putExtra(titleExtra, title)
     //   intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent,
        )
        showAlert(time, title)


}

    private fun showAlert(time: Long, title: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(requireContext().applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(requireContext().applicationContext)

        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setTitle("Notification Scheduled")
            .setMessage("Title:" + title +
            "\nAt:" + dateFormat.format(date) + "" + timeFormat.format(time))
            .setPositiveButton("Okay") {_,_->}
            .show()
/*
  var funct = (10 * doubleWeight + 6.25 * intHeight - 5 * intAge + 5)

            var value = findViewById<TextView>(R.id.answer)
            value.text = "Ответ: "+funct.toString()
 */
    }

    private fun getTime(): Long {
     //   val time = requireView().findViewById<TextInputLayout>(R.id.time.minutes.toInt())
        val minute = requireView().findViewById<TimePicker>(R.id.time).minute
        val hour = requireView().findViewById<TimePicker>(R.id.time).hour
        val day = requireView().findViewById<DatePicker>(R.id.data).dayOfMonth
        val month = requireView().findViewById<DatePicker>(R.id.data).month
        val year = requireView().findViewById<DatePicker>(R.id.data).year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis

    }


    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroyView() {
            _binding = null
            super.onDestroyView()
        }
    }

