package dev.brahmkshatriya.echo.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import dev.brahmkshatriya.echo.R
import dev.brahmkshatriya.echo.databinding.FragmentSettingsContainerBinding
import dev.brahmkshatriya.echo.ui.UiViewModel.Companion.applyBackPressCallback
import dev.brahmkshatriya.echo.ui.UiViewModel.Companion.applyContentInsets
import dev.brahmkshatriya.echo.ui.UiViewModel.Companion.applyInsets
import dev.brahmkshatriya.echo.utils.ui.AnimationUtils.setupTransition
import dev.brahmkshatriya.echo.utils.ui.AutoClearedValue.Companion.autoCleared
import dev.brahmkshatriya.echo.utils.ui.FastScrollerHelper
import dev.brahmkshatriya.echo.utils.ui.UiUtils.onAppBarChangeListener

abstract class BaseSettingsFragment : Fragment() {

    abstract val title: String?
    abstract val creator: () -> PreferenceFragmentCompat

    private var binding: FragmentSettingsContainerBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupTransition(view)

        applyBackPressCallback()
        binding.appBarLayout.onAppBarChangeListener { offset ->
            binding.toolbarOutline.alpha = offset
        }
        binding.title.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.title.title = title
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, creator())
            .commit()

        view.post {
            runCatching {
                binding.fragmentContainer.getFragment<PreferenceFragmentCompat>().listView?.apply {
                    clipToPadding = false
                    applyInsets { applyContentInsets(it) }
                    isVerticalScrollBarEnabled = false
                    FastScrollerHelper.applyTo(this)
                }
            }
        }
    }

}