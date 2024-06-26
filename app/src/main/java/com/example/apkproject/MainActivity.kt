package com.example.apkproject

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.apkproject.databinding.ActivityMainBinding
import com.example.apkproject.model.Constants.SERVER_URI
import com.example.apkproject.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import info.mqtt.android.service.MqttAndroidClient
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

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
        observeLiveData()

    }
    private fun observeLiveData(){
        viewModel.currentIndexLiveData.observe(this){currentIndex->
          if (currentIndex>=0){
              showImage(currentIndex)
          }
        }
    }

    private fun showImage(currentIndex:Int) {
        Log.d("asdf", "showNextImage: ")
        if (currentIndex < imagesId.size) {
            loadCurrentImage(currentIndex)
        }
    }
    private fun loadCurrentImage(currentIndex:Int) {
        binding.image.setImageResource(imagesId[currentIndex])
    }
}