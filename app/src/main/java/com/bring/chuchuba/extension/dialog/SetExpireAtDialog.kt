package com.bring.chuchuba.extension.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.TimePicker
import com.bring.chuchuba.R
import java.text.SimpleDateFormat
import java.util.*

class SetExpireAtDialog : Dialog {
    constructor(context : Context) : super(context){}
    constructor(context : Context, callback : (String) -> Unit) : super(context){
        this.callback = callback
    }
    private var callback : ((String) -> Unit) = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND or WindowManager.LayoutParams.FLAG_FULLSCREEN
        layoutParams.dimAmount = 0.8f
        window?.attributes = layoutParams
        window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        setUpContents()
    }

    private fun setUpContents() {
        setContentView(R.layout.date_time_picker)
        val setButton = findViewById<View>(R.id.date_time_set)
        setButton.setOnClickListener {
            val datePicker = findViewById<DatePicker>(R.id.date_picker)
            val timePicker = findViewById<TimePicker>(R.id.time_picker)
            val calendar: Calendar = GregorianCalendar(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth,
                timePicker.hour,
                timePicker.minute
            )
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val time : String = sdf.format(Date(calendar.timeInMillis))
            callback(time)
            dismiss()
        }
    }

    override fun show() {
        super.show()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }
}