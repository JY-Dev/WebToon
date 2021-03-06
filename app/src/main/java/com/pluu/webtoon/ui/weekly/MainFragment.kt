package com.pluu.webtoon.ui.weekly

import android.animation.AnimatorSet
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.tabs.TabLayoutMediator
import com.pluu.core.utils.lazyNone
import com.pluu.utils.ProgressDialog
import com.pluu.utils.viewbinding.viewBinding
import com.pluu.webtoon.Const
import com.pluu.webtoon.R
import com.pluu.webtoon.databinding.FragmentToonBinding
import com.pluu.webtoon.di.provider.NaviColorProvider
import com.pluu.webtoon.domain.usecase.WeeklyUseCase
import com.pluu.webtoon.event.ThemeEvent
import com.pluu.webtoon.utils.animator.animatorStatusBarColor
import com.pluu.webtoon.utils.animator.animatorToolbarColor
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/**
 * Main View Fragment
 * Created by pluu on 2017-05-07.
 */
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_toon) {

    private var isFirstDlg = true

    private val loadDlg: Dialog by lazyNone {
        ProgressDialog.create(requireContext(), R.string.msg_loading).apply {
            setCancelable(false)
        }
    }

    @Inject
    lateinit var serviceApi: WeeklyUseCase

    @Inject
    lateinit var colorProvider: NaviColorProvider

    private val binding by viewBinding(FragmentToonBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setServiceTheme()

        binding.viewPager.apply {
            adapter = MainFragmentAdapter(
                fm = parentFragmentManager,
                serviceApi = serviceApi,
                lifecycle = viewLifecycleOwner.lifecycle
            )
            // 금일 기준으로 ViewPager 기본 표시
            setCurrentItem(serviceApi.todayTabPosition, false)
        }

        TabLayoutMediator(binding.slidingTabLayout, binding.viewPager) { tab, position ->
            tab.text = serviceApi.getWeeklyTabName(position)
        }.attach()

        registerStartEvent()
        registerLoadEvent()
    }

    // 선택한 서비스에 맞는 컬러 테마 변경
    private fun setServiceTheme() {
        val color = colorProvider.getTitleColor()
        val colorVariant = colorProvider.getTitleColorVariant()
        val activity = activity

        if (activity is AppCompatActivity) {
            val toolbarAnimator = activity.animatorToolbarColor(color)
            val statusBarAnimator = activity.animatorStatusBarColor(colorVariant)

            AnimatorSet().apply {
                duration = 250L
                playTogether(toolbarAnimator, statusBarAnimator)
            }.start()
        }

        setFragmentResult(
            Const.resultTheme, bundleOf(
                KEY_COLOR to ThemeEvent(color, colorVariant)
            )
        )
        binding.slidingTabLayout.setSelectedTabIndicatorColor(color)
    }

    private fun registerLoadEvent() {
        setFragmentResultListener(Const.resultEpisodeLoaded) { _, _ ->
            eventLoadedEvent()
        }
    }

    private fun registerStartEvent() {
        setFragmentResultListener(Const.resultEpisodeStart) { _, _ ->
            eventStartEvent()
        }
    }

    private fun eventStartEvent() {
        if (isFirstDlg) {
            Timber.d("eventStartEvent")
            loadDlg.show()
            isFirstDlg = false
        }
    }

    private fun eventLoadedEvent() {
        if (!isFirstDlg) {
            Timber.d("eventLoadedEvent")
            loadDlg.dismiss()
        }
    }

    companion object {
        const val KEY_COLOR = "color"

        fun newInstance() = MainFragment()
    }
}
