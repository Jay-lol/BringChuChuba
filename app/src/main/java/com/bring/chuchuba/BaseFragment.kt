package com.bring.chuchuba

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bring.chuchuba.viewmodel.HomeViewModel
import com.bring.chuchuba.viewmodel.home.buildlogic.HomeInjector

abstract class BaseFragment<T : ViewDataBinding>(private val layoutName: FragmentLayout) : Fragment() {

    lateinit var binding: T
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(
                requireActivity(),
                HomeInjector().provideViewModelFactory()
        ).get(
                HomeViewModel::class.java
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val layout: Int = when (layoutName) {
            is FragmentLayout.Home -> R.layout.fragment_home
            is FragmentLayout.CreateMission -> R.layout.create_mission_fragment
            is FragmentLayout.Calendar -> R.layout.fragment_calendar
            is FragmentLayout.MyInfo -> R.layout.fragment_my_info
            is FragmentLayout.MissionDetailFragment -> R.layout.fragment_mission_detail
            is FragmentLayout.MyMissionFragment -> R.layout.fragment_my_mission
            is FragmentLayout.MyMissionListFragment -> R.layout.fragment_my_mission_list
            is FragmentLayout.MissionList -> R.layout.fragment_mission_list
        }
        binding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    open fun observeViewModels() {}
}