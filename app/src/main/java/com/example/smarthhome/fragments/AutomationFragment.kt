package com.example.smarthhome.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smarthhome.databinding.FragmentAutomationBinding

class AutomationFragment : Fragment() {

    private var _binding: FragmentAutomationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAutomationBinding.inflate(inflater, container, false)
        return binding.root
    }

}