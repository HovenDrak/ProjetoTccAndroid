package com.example.smarthhome.ui.fragment

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smarthhome.databinding.FragmentEventsHistoryBinding
import com.example.smarthhome.repository.ApiRepository
import com.example.smarthhome.repository.dao.EventsDao
import com.example.smarthhome.service.EventsHistory
import org.koin.android.ext.android.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventsHistoryFragment : Fragment(){

    private var _binding: FragmentEventsHistoryBinding? = null
    private val binding get() = _binding!!

    private val eventsDao: EventsDao by inject()
    private val eventsHistory: EventsHistory by inject()

    private val apiRepository = ApiRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEventsHistoryBinding.inflate(inflater, container, false)
        eventsHistory.setBinding(binding, requireContext())
        eventsHistory.configAdapter(eventsDao.getAllEvents())

        val dateNow = LocalDateTime.now()
        apiRepository.getDayLog(requireContext(), dateNow.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))

        val listener = OnDateSetListener {_, year, month, dayOfMonth ->
            val newMonth = if (month + 1 < 10) "0${month +1}" else month +1
            val newDay = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
            val dateSelect = "$newDay-$newMonth-$year 00:00:00"
            eventsHistory.activeLoading()

            apiRepository.getDayLog(requireContext(), dateSelect)
        }

        val datePickerDialog = DatePickerDialog(requireContext(), listener, dateNow.year, dateNow.monthValue - 1, dateNow.dayOfMonth)

        binding.btnEventsDate.setOnClickListener {
            datePickerDialog.show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onResume() {
        eventsHistory.refreshAdapter(eventsDao.getAllEvents())
        super.onResume()
    }
}