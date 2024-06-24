package com.example.apkproject.view

import androidx.fragment.app.Fragment
import com.example.apkproject.R
import com.example.apkproject.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val imagesId= arrayListOf(
        R.drawable.test1,
        R.drawable.test3,
        R.drawable.test4,
        R.drawable.test5,
        R.drawable.test6
    )
    var currentIndex = 0

    /*
       ovride fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)


       }

       override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

           binding.backButton.setOnClickListener{
             showPreviousImage()

           }
           binding.nextButton.setOnClickListener{
               showNextImage()
           }
           loadCurrentImage()
           createMqttClient()
           return inflater.inflate(R.layout.fragment_main, container, false)
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
       */




}