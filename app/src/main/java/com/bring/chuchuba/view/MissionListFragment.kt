package com.bring.chuchuba.view

import android.graphics.Color
import android.os.Bundle
import android.os.PatternMatcher
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bring.chuchuba.BaseFragment
import com.bring.chuchuba.FragmentLayout
import com.bring.chuchuba.R
import com.bring.chuchuba.adapter.mission.MissionListAdapter
import com.bring.chuchuba.databinding.FragmentMissionListBinding
import com.bring.chuchuba.model.mission.MissionsItem
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class MissionListFragment(val status: Int) :
    BaseFragment<FragmentMissionListBinding>(FragmentLayout.MissionList) {

    private val TAG: String = "로그 ${this.javaClass.simpleName}"
    private lateinit var adapter: MissionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.homeVm = homeViewModel
        settingAdapter()
        observeViewModels()
        return view
    }

    override fun observeViewModels() {
        homeViewModel.missionData.observe(
            viewLifecycleOwner,
            Observer { missionList ->
                Log.d(TAG, "HomeFragment ~ missiondata() called ")
                missionList ?: return@Observer
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
                for (i in missionList){
                    try {
                        val millisEx = sdf.parse(i.expireAt).time
                        val millisSt = sdf.parse(i.createdAt).time
                        val now : Long = System.currentTimeMillis()
                        if (now <= millisEx)
                            Log.d(TAG, "observeViewModels() called ${((now - millisSt)/(millisEx - millisSt).toDouble() * 100).toInt()}")
                    }catch (e : Exception){

                    }
                }
                var missionCnt : SpannableStringBuilder
                when (status) {
                    0 -> adapter.submitList(missionList.also { missionCnt= SpannableStringBuilder("총 ${it.size}개의 미션이 있습니다.") })
                    1 -> adapter.submitList(missionList.filter { it.status == "todo" }.also { missionCnt = SpannableStringBuilder("총 ${it.size}개의 미션이 있습니다.") })
                    2 -> adapter.submitList(missionList.filter { it.status == "inProgress" }.also { missionCnt = SpannableStringBuilder("총 ${it.size}개의 미션이 있습니다.") })
                    3 -> adapter.submitList(missionList.filter { it.status == "complete" }.also { missionCnt = SpannableStringBuilder("총 ${it.size}개의 미션이 있습니다.") })
                    else -> adapter.submitList(missionList.filter { calDone(it.expireAt) &&  it.status != "complete" }.also { missionCnt = SpannableStringBuilder("총 ${it.size}개의 미션이 있습니다.") })
                }
                // 숫자만 강조
                missionCnt.setSpan(ForegroundColorSpan(Color.parseColor("#8587FE")), 2, missionCnt.length - 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                binding.missionCnt.text = missionCnt
            }
        )
    }

    private fun calDone(expireAt: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val now =  sdf.format(Date(System.currentTimeMillis()))
        try {
            val x = sdf.parse(now)
            val y = sdf.parse(expireAt)
            return x.after(y)
        } catch (e : Exception){
            return true
        }
    }

    private fun settingAdapter() {
        adapter = MissionListAdapter(callback)
        binding.homeRecyclerView.adapter = adapter
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    val callback: (MissionsItem) -> Unit = { mission ->
        val transaction =
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MissionDetailFragment(mission))
        transaction.addToBackStack(null)
        transaction.commit()
    }
}