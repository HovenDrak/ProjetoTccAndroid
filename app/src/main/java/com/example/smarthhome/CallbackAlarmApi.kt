package com.example.smarthhome

import com.example.smarthhome.model.Status

interface CallbackAlarmApi {
    fun reveiverApi(list: List<Status>?)
}