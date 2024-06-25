package com.example.apkproject

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apkproject.Message.MessageDataClass
import com.example.apkproject.databinding.ActivityMainBinding
import com.example.apkproject.model.constant.SERVER_URI
import com.google.gson.Gson
import info.mqtt.android.service.MqttAndroidClient
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mqtt: MqttAndroidClient
    var currentIndex = 0
    private lateinit var mqttHandler: MqttHandler
    private val clientId = MqttClient.generateClientId()

    private val imagesId = arrayListOf(
        R.drawable.test1,
        R.drawable.test3,
        R.drawable.test4,
        R.drawable.test5,
        R.drawable.test6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mqttHandler = MqttHandler()
        mqttHandler.connect(SERVER_URI, clientId.toString())

        binding.backButton.setOnClickListener {
            showPreviousImage()

        }
        binding.nextButton.setOnClickListener {
            showNextImage()
        }
        loadCurrentImage()
        createMqttClient()

        val clientId = MqttClient.generateClientId()
        //  mqttClient = MqttAndroidClient(applicationContext, SERVER_URI,clientId)

        lifecycleScope.launch {
            mqtt = MqttAndroidClient(
                this@MainActivity, SERVER_URI, UUID.randomUUID().toString()
            )

            mqtt.connect(MqttConnectOptions(), null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    println("Connection successfull")
                    mqtt.subscribe("testtest", 1)

                    val clientId = MqttClient.generateClientId()
                    // mqttClient = MqttAndroidClient(applicationContext, SERVER_URI, clientId)

                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    exception?.printStackTrace()

                }

            })

            mqtt.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    println("Connection lost")
                }
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    println("$topic: ${message?.payload?.toString(Charsets.UTF_8)}")
                    val msg = message?.payload?.toString(Charsets.UTF_8)
                    Log.d("asdf", msg.toString())
                    val parsedMessage = Gson().fromJson(msg, MessageDataClass::class.java)
                    when (parsedMessage.msg) {
                        "Next" -> showNextImage()
                        "Back" -> showPreviousImage()
                        else -> {
                            Toast.makeText(this@MainActivity, parsedMessage.msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {

                }
            })


        }
    }


    private fun createMqttClient() {

    }

    protected override fun onDestroy() {
        mqttHandler.disconnect()
        super.onDestroy()

    }

    private fun publishMessage(topic: String?, message: MqttMessage?) {
        Toast.makeText(this, "Publishing message" + message, Toast.LENGTH_LONG).show()
        mqttHandler.publish(topic, message.toString())
    }

    private fun subscribe(topic: String?) {
        Toast.makeText(this, "Subscribing to topic", Toast.LENGTH_LONG).show()
        mqttHandler.subscribe(topic)
    }

    private fun showNextImage() {
        Log.d("asdf", "showNextImage: ")
        if (currentIndex < imagesId.size) {
            currentIndex++
            loadCurrentImage()
        }
    }

    private fun showPreviousImage() {
        if (currentIndex > 0) {
            currentIndex--
            loadCurrentImage()
        }
    }

    private fun loadCurrentImage() {
        binding.image.setImageResource(imagesId[currentIndex])
    }


}