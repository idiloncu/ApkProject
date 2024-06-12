package com.example.apkproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.apkproject.R.drawable.test1
import com.example.apkproject.R.drawable.test3
import com.example.apkproject.R.drawable.test4
import com.example.apkproject.R.drawable.test5
import com.example.apkproject.R.drawable.test6
import com.example.apkproject.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    val arrayList = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arrayList.add(test1)
        arrayList.add(test3)
        arrayList.add(test4)
        arrayList.add(test5)
        arrayList.add(test6)

        binding.backButton.setOnClickListener{
            for (images in arrayList){
                images.setImageResource(R.drawable.test1)
            }

        }
        binding.nextButton.setOnClickListener{
            for (images in arrayList){
                images.setImageResource(R.drawable.test1)
            }

        }
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}

private fun <E> MutableList<E>.add(element: Any) {

}


