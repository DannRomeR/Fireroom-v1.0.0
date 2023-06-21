package com.example.fireroomv100.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fireroomv100.databinding.OnboardingBinding

class OnboardingFragment : Fragment() {

    private var _binding: OnboardingBinding?=null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val onboardingViewModel =
            ViewModelProvider(this).get(OnboardingViewModel::class.java)

        _binding = OnboardingBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val textView: TextView = binding.textSlideshow
        onboardingViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}