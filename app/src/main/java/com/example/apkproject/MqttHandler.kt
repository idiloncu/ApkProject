package com.example.apkproject

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.json.JSONStringer

 class MqttHandler {
    private var client: MqttClient? = null
    fun connect(brokerUrl: String?, clientId: String?) {
        try {
            val persistence = MemoryPersistence()
            client = MqttClient(brokerUrl, clientId, persistence)
            val connectOptions = MqttConnectOptions()
            connectOptions.isCleanSession = true

            client!!.connect(connectOptions)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            client!!.disconnect()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String?, message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            client!!.publish(topic, mqttMessage)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String?) {
        try {
            client!!.subscribe(topic)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
      fun serialize(message: MqttMessage){

      }
}