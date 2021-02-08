package com.bring.chuchuba.extension

import android.annotation.SuppressLint
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("missionStatus")
internal fun setStatus(view: TextView, status: String) {
    when(status){
        "todo" -> view.text = "시작 가능"
        "inProgress" -> view.text = "진행 중"
        "complete" -> view.text = "완료"
        else -> view.text = "알 수 없음"
    }
}
@SuppressLint("SimpleDateFormat")
@BindingAdapter("progressStatusFirst", "progressStatusSecond")
internal fun setProgressGauge(view: ProgressBar, createAt: String, expireAt: String){
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
    try {
        val millisEx : Long = sdf.parse(expireAt).time
        val millisSt : Long = sdf.parse(createAt).time
        val now : Long = System.currentTimeMillis()

        view.progress = ((now - millisSt)/(millisEx - millisSt).toDouble() * 100).toInt()

    }catch (e : Exception){
    }
}