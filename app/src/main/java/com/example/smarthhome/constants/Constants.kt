package com.example.smarthhome.constants

object Constants{

    // -------------------- CONSTANTS ERRORS --------------------//
    const val ERROR_LOAD_STATE_MQTT = "NÃO FOI POSSIVEL ATUALIZAR O STATUS PELO MQTT"
    const val ERROR_LOAD_STATE_API = "ERRO AO CARREGAR STATUS"
    const val ERROR_SEND_CMND = "Erro ao enviar comando"

    // -------------------- CONSTANTS DATABASE --------------------//
    const val NAME_DATABASE = "smarthome.db"

    // -------------------- MQTT CONSTANTS ALARM --------------------//
    val LIST_TOPIC_ALARM = listOf("alarme", "setor1", "setor2", "setor3", "setor4", "error/alarme")

    // -------------------- MQTT CONSTANTS AUTOMATION --------------------//
    val LIST_TOPIC_LIGHT = listOf("light1", "light2", "light3", "light4")
    const val CMND_MQTT_LIGHT_OFF = "\"desligado\""
    const val CMND_MQTT_LIGHT_ON = "\"ligado\""
    const val CMND_LIGHT_OFFLINE = "offline"
    const val CMND_LIGHT_OFF = "desligado"
    const val CMND_LIGHT_ON = "ligado"

    // -------------------- MQTT CONSTANTS GENERAL --------------------//
    const val HOST_MQTT = "ssl://bbfb08f6f1b84ffebf8b0c4fbbcd0e90.s1.eu.hivemq.cloud:8883"
    const val PASSWORD_MQTT = "67UserAndroid67"
    const val CLIENTID_MQTT = "android_client"
    const val USER_MQTT = "userAndroid"
    const val TAG_MQTT = "MQTT"

}