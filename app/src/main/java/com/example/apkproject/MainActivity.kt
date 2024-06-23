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
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.UUID
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)


        lifecycleScope.launch {
            val mqtt = MqttAndroidClient(
                this@MainActivity, "tcp://deneme-234@broker.mqtt.cool:1883", UUID.randomUUID().toString()
            )

            mqtt.connect(MqttConnectOptions(),null,object:IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    println("Connection successfull")
                    mqtt.subscribe("testtest", 1)
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
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {

                }
            })

        }
    }

}