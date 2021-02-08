package com.bring.chuchuba

sealed class FragmentLayout {
    object Home : FragmentLayout()
    object Calendar : FragmentLayout()
    object MyInfo : FragmentLayout()
    object CreateMission : FragmentLayout()
    object MissionDetailFragment : FragmentLayout()
    object MyMissionFragment : FragmentLayout()
    object MyMissionListFragment : FragmentLayout()
    object MissionList : FragmentLayout()
}