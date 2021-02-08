package com.bring.chuchuba.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bring.chuchuba.BaseFragment
import com.bring.chuchuba.FragmentLayout
import com.bring.chuchuba.R
import com.bring.chuchuba.adapter.CustomFragmentAdapter
import com.bring.chuchuba.adapter.HomeFragmentAdapter
import com.bring.chuchuba.adapter.mission.MissionListAdapter
import com.bring.chuchuba.databinding.FragmentHomeBinding
import com.bring.chuchuba.model.mission.MissionsItem
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentLayout.Home) {

    private val TAG: String = "로그 ${this.javaClass.simpleName}"

    private lateinit var completedAdapter: MissionListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.homeVm = homeViewModel
        binding.homeFrag = this
//        settingAdapter()
        setMissionFilter()
        observeViewModels()
        return view
    }

    private fun setMissionFilter() {
        val tabTextList: List<String> = listOf(
               "전체", "모집중", "진행중", "완료됨", "기한만료"
        )
        binding.homeViewPager.adapter = HomeFragmentAdapter(requireActivity())
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }

    fun createMission() {
        val transaction =
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateMissionFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun settingAdapter() {
//        completedAdapter = MissionListAdapter(callback)
//        binding.missionCompleteRecyclerView.adapter = completedAdapter
//        binding.missionCompleteRecyclerView.layoutManager = LinearLayoutManager(context)
    }


    override fun observeViewModels() {
        homeViewModel.myInfo.observe(
            viewLifecycleOwner,
            Observer { member ->
                member ?: return@Observer
            }
        )

        homeViewModel.missionData.observe(
            viewLifecycleOwner,
            Observer { missionList ->
                Log.d(TAG, "HomeFragment ~ missiondata() called $missionList")
                missionList ?: return@Observer

//                adapter.submitList(missionList.filter { it.status != "complete" })

//                completedAdapter.submitList(missionList.filter { it.status == "complete" })
            }
        )
    }



    val callback: (MissionsItem) -> Unit = { mission ->
        val transaction =
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MissionDetailFragment(mission))
        transaction.addToBackStack(null)
        transaction.commit()
    }

}