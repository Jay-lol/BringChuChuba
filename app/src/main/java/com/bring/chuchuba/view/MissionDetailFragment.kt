package com.bring.chuchuba.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.bring.chuchuba.BaseFragment
import com.bring.chuchuba.FragmentLayout
import com.bring.chuchuba.databinding.FragmentMissionDetailBinding
import com.bring.chuchuba.extension.showToast
import com.bring.chuchuba.model.mission.MissionsItem
import com.bring.chuchuba.viewmodel.home.buildlogic.HomeEvent

class MissionDetailFragment(val mission: MissionsItem) : BaseFragment<FragmentMissionDetailBinding>(FragmentLayout.MissionDetailFragment) {
    private val TAG: String = "로그 ${this.javaClass.simpleName}"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.mdFrag = this
        observeViewModels()
        restrictButton()
        return view
    }

    private fun restrictButton() {
        val myId = homeViewModel.myInfo.value?.id
        if(mission.status == "complete"){
            binding.contractMissionButton.visibility = View.GONE
            binding.completeMissionButton.visibility = View.GONE
        }
        else if (mission.client.id == myId)
            binding.contractMissionButton.visibility = View.GONE
        else
            binding.completeMissionButton.visibility = View.GONE
    }

    override fun observeViewModels() {


    }

    /**
     * 트랜잭션 처리를 뷰모델의 이벤트가 끝난다음에 호출해야함.
     * 콜백이나 라이브데이터를 활용해야할듯??
     */
    fun completeMission(){
        homeViewModel.handleEvent(HomeEvent.OnCompleteMission(mission))
        val transaction : FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction().remove(this)
        this.showToast("미션 완료 성공")
        transaction.commit()
    }

    fun contractMission(){
        homeViewModel.handleEvent(HomeEvent.OnContractMission(mission))
        val transaction : FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction().remove(this)
        this.showToast("미션 수주 성공")
        transaction.commit()
    }

}