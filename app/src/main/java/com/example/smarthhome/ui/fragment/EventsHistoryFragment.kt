package com.example.smarthhome.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthhome.databinding.FragmentEventsHistoryBinding
import com.example.smarthhome.repository.ApiRepository
import com.example.smarthhome.repository.dao.EventsDao
import com.example.smarthhome.service.EventsHistory
import com.example.smarthhome.ui.recyclerview.adapter.ListEventAdapter
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent

class EventsHistoryFragment : Fragment() {

    private var _binding: FragmentEventsHistoryBinding? = null
    private val binding get() = _binding!!

    private val eventsDao: EventsDao by inject()
    private val eventsHistory: EventsHistory by inject()

    private val apiRepository = ApiRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsHistoryBinding.inflate(inflater, container, false)
        eventsHistory.setBinding(binding, requireContext())
        apiRepository.getDayLog(requireContext())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        eventsHistory.refreshRecyclerView(eventsDao.getAllEvents())
    }
}