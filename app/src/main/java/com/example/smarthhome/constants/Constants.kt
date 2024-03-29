package com.example.smarthhome.constants

object Constants{

    // -------------------- CONSTANTS ERRORS --------------------//
    const val ERROR_LOAD_STATE_MQTT = "NÃO FOI POSSIVEL ATUALIZAR O STATUS PELO MQTT"
    const val ERROR_LOAD_API = "ERRO AO CARREGAR API"
    const val ERROR_SEND_CMND = "Erro ao enviar comando"

    // -------------------- CONSTANTS DATABASE --------------------//
    const val NAME_DATABASE = "smarthome.db"

    // -------------------- MQTT CONSTANTS ALARM --------------------//
    val LIST_NAME_SENSORS = arrayListOf("Porta Sala", "Porta Fundo", "Porta Sacada")
    val LIST_TOPIC_ALARM = listOf("alarme", "setor1", "setor2", "setor3")

    const val TOPIC_CMND_ALARM = "cmnd/alarme"
    const val TOPIC_CMND_ALARM_BYPASS = "cmnd/alarme/bypass"

    const val NAME_SETOR_1 = "Porta Sala"
    const val NAME_SETOR_2 = "Porta Fundo"
    const val NAME_SETOR_3 = "Porta Sacada"
    const val NAME_SETOR_4 = "Janela"

    const val CMND_MQTT_SENSOR_BYPASS = "\"bypassed\""
    const val CMND_MQTT_SENSOR_CLOSE = "\"fechado\""
    const val CMND_MQTT_SENSOR_OPEN = "\"aberto\""

    const val CMND_API_SENSOR_BYPASS = "bypassed"
    const val CMND_API_SENSOR_CLOSE = "fechado"
    const val CMND_API_SENSOR_OPEN = "aberto"
    const val CMND_SENSOR_DEFAULT = "offline"

    const val CMND_MQTT_GARAGE_CLOSE = CMND_MQTT_SENSOR_CLOSE
    const val CMND_MQTT_GARAGE_OPEN = CMND_MQTT_SENSOR_OPEN
    const val CMND_API_GARAGE_CLOSE = CMND_API_SENSOR_CLOSE
    const val CMND_API_GARAGE_OPEN = CMND_API_SENSOR_OPEN

    const val CMND_MQTT_VIOLED = "\"disparado\""
    const val CMND_MQTT_DISARM = "\"desarmado\""
    const val CMND_MQTT_ARM = "\"armado\""

    const val SEND_CMND_DISARM = "[{\"newState\":\"desarmado\"}, {\"user\":\"Mobile\"}]"
    const val SEND_CMND_ARM = "[{\"newState\":\"armado\"}, {\"user\":\"Mobile\"}]"

    const val CMND_API_VIOLED = "disparado"
    const val CMND_API_DISARM = "desarmado"
    const val CMND_API_ARM = "armado"

    // -------------------- MQTT CONSTANTS AUTOMATION --------------------//
    const val TOPIC_AUTOMATION = "automation"
    const val TOPIC_ALARM = "alarm"

    val LIST_NAMES_AUTOMATION = listOf("Luz da Sala", "Luz do Quarto", "Luz da Cozinha", "Luz do Banheiro")
    val LIST_TOPIC_AUTOMATION = listOf("light1", "light2", "light3", "light4", "garage")

    const val TOPIC_LIGHT = "light"

    const val CMND_MQTT_LIGHT_OFF = "\"desligado\""
    const val CMND_MQTT_LIGHT_ON = "\"ligado\""

    const val CMND_LIGHT_OFFLINE = "offline"
    const val CMND_LIGHT_OFF = "desligado"
    const val CMND_LIGHT_ON = "ligado"

    const val BACKGROUND_COLOR_LIGHT_OFFLINE ="#7B7B7B"
    const val BACKGROUND_COLOR_LIGHT_OFF = "#DCDCDC"
    const val BACKGROUND_COLOR_LIGHT_ON = "#FFFFFF"

    const val TEXT_LIGHT_OFFLINE = "Offline"
    const val TEXT_LIGHT_OFF = "Desligado"
    const val TEXT_LIGHT_ON = "Ligado"

    // -------------------- CONSTANTS GENERAL --------------------//
    const val HOST = "http://54.174.25.158/"
    const val PATH_API_AUTOMATION = "/automation/status/all"
    const val PATH_API_HOME = "/alarm/status/all"
    const val PATH_API_LOG_ALL = "/all/log"
    const val PATH_API_LOG_DAY = "/log/day"

    const val HOST_MQTT = "ssl://bbfb08f6f1b84ffebf8b0c4fbbcd0e90.s1.eu.hivemq.cloud:8883"
    const val CLIENT_ID_MQTT = "android_client"
    const val PASSWORD_MQTT = "67UserAndroid67"
    const val USER_MQTT = "userAndroid"
    const val TAG_MQTT = "MQTT"

}