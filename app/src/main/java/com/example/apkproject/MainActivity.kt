package com.example.apkproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.apkproject.databinding.ActivityMainBinding
import info.mqtt.android.service.MqttAndroidClient
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.UUID
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mqttClient:MqttAndroidClient
    var currentIndex = 0
    private val imagesId= arrayListOf(
        R.drawable.test1,
        R.drawable.test3,
        R.drawable.test4,
        R.drawable.test5,
        R.drawable.test6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        binding.backButton.setOnClickListener{
            showPreviousImage()

        }
        binding.nextButton.setOnClickListener{
            showNextImage()
        }
        loadCurrentImage()
        createMqttClient()



        lifecycleScope.launch {
            val mqtt = MqttAndroidClient(
                this@MainActivity, "@string/server_URI", UUID.randomUUID().toString()
            )

            mqtt.connect(MqttConnectOptions(),null,object:IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    println("Connection successfull")
                    mqtt.subscribe("testtest", 1)

                    val clientId = MqttClient.generateClientId()
                    mqttClient = MqttAndroidClient(applicationContext,"@string/server_URI",clientId)

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
                   runOnUiThread{
                        when(msg){
                            "@string/next" -> showNextImage()
                            "@string/back" -> showPreviousImage()
                            }
                        }
                    }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {

                }
            })
            val options=MqttConnectOptions().apply {
                isAutomaticReconnect=true
                isCleanSession = true
            }
            try {
                mqttClient.connect(options,null,object :IMqttActionListener{
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        mqttClient.subscribe("testtest",0)
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        exception?.printStackTrace()
                    }

                })
            }
            catch (e:MqttException){
                e.printStackTrace()
            }
        }
    }

    private fun createMqttClient() {

    }

    private fun showNextImage() {
        if (currentIndex<imagesId.size){
            currentIndex++
            loadCurrentImage()
        }
    }

    private fun showPreviousImage() {
        if (currentIndex>0){
            currentIndex--
            loadCurrentImage()
        }
    }

    private fun loadCurrentImage() {
        binding.image.setImageResource(imagesId[currentIndex])
    }
}