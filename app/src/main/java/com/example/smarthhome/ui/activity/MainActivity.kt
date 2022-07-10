@file:Suppress("DEPRECATION")

package com.example.smarthhome.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smarthhome.R
import com.example.smarthhome.databinding.ActivityMainBinding
import com.example.smarthhome.repository.MqttRepository
import com.example.smarthhome.ui.fragment.AutomationFragment
import com.example.smarthhome.ui.fragment.HomeFragment
import org.eclipse.paho.android.service.MqttAndroidClient
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val mqttClient: MqttAndroidClient by inject()
    private val mqttRepository = MqttRepository(mqttClient)
    private val homeFragment = HomeFragment()
    private val automationFragment = AutomationFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configMqtt()
        configNavigation()
        replaceFragment(homeFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttRepository.disconnect()
    }

    private fun configMqtt() {
        mqttRepository.connectMqtt()
    }

    private fun configNavigation() {
        binding.navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.txtHome -> replaceFragment(homeFragment)
                R.id.automation -> replaceFragment(automationFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}