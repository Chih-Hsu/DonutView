package com.example.customview.donutview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.customview.databinding.FragmentBBinding
import kotlin.random.Random

class BFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBBinding.inflate(inflater,container,false)


        binding.button2.setOnClickListener {

            val list = mutableListOf<Float>()
            for (i in 0..Random.nextInt(10)){
                val value = Random.nextFloat()
                list.add(value)
            }
            binding.donutChart.submitList(list)
        }

        return binding.root
    }
}