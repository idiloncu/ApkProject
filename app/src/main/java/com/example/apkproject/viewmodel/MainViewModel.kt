package com.example.apkproject.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apkproject.R
import com.example.apkproject.dataclass.MessageDataClass
import com.example.apkproject.model.Constants
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

@HiltViewModel

class MainViewModel @Inject constructor(
    private val mqttAndroidClient: MqttAndroidClient
):ViewModel(){

    var currentIndex = 0
    private val _currentIndexLiveData = MutableLiveData<Int>()
    val currentIndexLiveData:LiveData<Int> = _currentIndexLiveData
    //viewmodeli alıp vim daki indeksi observeet
    //gelen şndekse gore show ımage


    init {
        mqttMessage()
    }

    private fun mqttMessage(){
        viewModelScope.launch {


            mqttAndroidClient.connect(MqttConnectOptions(), null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    println("Connection successfull")
                    mqttAndroidClient.subscribe("testtest", 1)

                    val clientId = MqttClient.generateClientId()
                    // mqttClient = MqttAndroidClient(applicationContext, SERVER_URI, clientId)

                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    exception?.printStackTrace()

                }

            })

            mqttAndroidClient.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    println("Connection lost")
                }
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    println("$topic: ${message?.payload?.toString(Charsets.UTF_8)}")
                    val msg = message?.payload?.toString(Charsets.UTF_8)
                    Log.d("asdf", msg.toString())
                    val parsedMessage = Gson().fromJson(msg, MessageDataClass::class.java)
                    when (parsedMessage.msg) {
                        "Next" -> {
                        if(_currentIndexLiveData.value!=null){
                            var nextIndex = _currentIndexLiveData.value?.plus(1) ?: 0
                            _currentIndexLiveData.postValue(nextIndex)

                        }
                            else{
                                _currentIndexLiveData.postValue(1)
                        }
                        }
                        "Back" -> {
                            if(_currentIndexLiveData.value != null){
                               var index= _currentIndexLiveData.value?.minus(1)?:0
                                _currentIndexLiveData.postValue(index)
                            }
                            else{
                                _currentIndexLiveData.postValue(0)
                            }
                        }
                        else -> {
                        }
                    }

                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {

                }
            })


        }
    }


}