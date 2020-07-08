package com.example.passover

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        //약관동의
        view.findViewById<ToggleButton>(R.id.btn1_1).setOnClickListener {
            val btnToggle = it.findViewById<ToggleButton>(R.id.btn1_1)
            setToggleAgree(btnToggle)
        }
        view.findViewById<ToggleButton>(R.id.btn2_1).setOnClickListener {
            val btnToggle = it.findViewById<ToggleButton>(R.id.btn2_1)
            setToggleAgree(btnToggle)
        }

        //약관보기
        view.findViewById<ImageButton>(R.id.btn1_2).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_TermsFragment)
        }
        view.findViewById<ImageButton>(R.id.btn2_2).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_PolicyFragment)
        }
    }

    private fun setToggleAgree(btnToggle : ToggleButton){
        if(btnToggle.isChecked){
            btnToggle.setBackgroundResource(R.drawable.baseline_check_box_24)
        }else{
            btnToggle.setBackgroundResource(R.drawable.baseline_check_box_outline_blank_24)
        }
    }
}