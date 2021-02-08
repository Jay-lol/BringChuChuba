package com.bring.chuchuba.view

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bring.chuchuba.*
import com.bring.chuchuba.adapter.CustomFragmentAdapter
import com.bring.chuchuba.databinding.ActivityMainBinding
import com.bring.chuchuba.extension.showToast
import com.bring.chuchuba.viewmodel.HomeViewModel
import com.bring.chuchuba.viewmodel.home.buildlogic.HomeEvent
import com.bring.chuchuba.viewmodel.home.buildlogic.HomeInjector
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val  TAG : String = "로그 ${this.javaClass.simpleName}"

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        handleDeepLink()
        homeViewModel = ViewModelProvider(
                this,
                HomeInjector().provideViewModelFactory()
        ).get(
                HomeViewModel::class.java
        )
        
        connectAdapter()
        getMyInfo()
        observeViewModels()
    }

    private fun observeViewModels() {
        homeViewModel.myInfo.observe(
                this,
                Observer { member ->
                    member ?: return@Observer
                    showToast("${member.nickname ?: "새로운 참가자"}님 환영합니다!")
                }
        )
        homeViewModel.jobSucceedOrFail.observe(this) { msg ->
            this.showToast(msg)
        }
    }

    private fun getMyInfo() {
        Log.d(TAG, "getMemberId")
        homeViewModel.handleEvent(HomeEvent.OnLogin)
    }

    // 뷰 페이저와 프레그먼트,탭레이아웃 연결
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun connectAdapter() {
        val tabIconList: List<Drawable?> = listOf(
                resources.getDrawable(R.drawable.home, null),
                resources.getDrawable(R.drawable.bookmark, null),
                resources.getDrawable(R.drawable.mypage, null),
        )
        binding.mainViewpager.adapter = CustomFragmentAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.mainViewpager) { tab, position ->
            tab.icon = tabIconList[position]
        }.attach()
        /**
         * 메인 탭은 슬라이드 안되게 설정. 홈프레그먼트의 필터 부분만 슬라이드 가능하도록 통일
         */
        binding.mainViewpager.isUserInputEnabled = false
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0 &&
                moveTaskToBack(false)
        ) return
        super.onBackPressed()
    }

    private fun handleDeepLink() {
        Firebase.dynamicLinks
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    // Get deep link from result (may be null if no link is found)
                    val deeplinkData = intent.data
                    deeplinkData ?: return@addOnSuccessListener
                    Log.d(TAG, "MainActivity ~ handleDeepLink() called ${deeplinkData.getQueryParameters("fKey")}")
                    val familyId = deeplinkData.getQueryParameters("fKey")[0]
                    homeViewModel.handleEvent(HomeEvent.OnJoinFamily(familyId))

                    // pendingDynamicLinkData는 계속 null만 나옴
                    Log.d(TAG, "MainActivity ~ handleDeepLink() called $pendingDynamicLinkData")
                    if (pendingDynamicLinkData == null) {
                        Log.d(TAG, "No have dynamic link")
                        return@addOnSuccessListener
                    }
                    val deepLink: Uri = pendingDynamicLinkData.link!!
                    Log.d(TAG, "deepLink: $deepLink")

                    // Handle the deep link. For example, open the linked
                    // content, or apply promotional credit to the user's
                    // account.
                    // ...
                    when (deepLink.lastPathSegment) {
                        "FAMILY_CODE" -> {
                            val code: String? = deepLink.getQueryParameter("FAMILY_CODE")
                            Log.d(TAG, "MainActivity ~ handleDeepLink() called $code")
                        }
                    }
                    // ...
                }
                .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }

    }

}