package com.example.news.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news.Interface.Communicator
import com.example.news.R
import kotlinx.android.synthetic.main.fragment_screen1.*

private lateinit var communicator: Communicator

class Screen1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_screen1, container, false)
        }

    private fun onBussinessBtn(view: View){
        communicator = activity as Communicator
        communicator.passDataCom("business")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_btn.setOnClickListener {
//            val manager = requireActivity().supportFragmentManager
//            manager.beginTransaction().remove(Screen1()).commit()
            communicator = activity as Communicator
            communicator.hideCategoryFragment()
        }

        businessBtn.setOnClickListener {
            onBussinessBtn(view)
            super.isHidden()
        }
        healthBtn.setOnClickListener {
            communicator = activity as Communicator
            communicator.passDataCom("health")
        }
        sportsBtn.setOnClickListener {
            communicator = activity as Communicator
            communicator.passDataCom("sports")
        }
        techBtn.setOnClickListener {
            communicator = activity as Communicator
            communicator.passDataCom("technology")
        }
        scienceBtn.setOnClickListener {
            communicator = activity as Communicator
            communicator.passDataCom("science")
        }
        entertainmentBtn.setOnClickListener {
            communicator = activity as Communicator
            communicator.passDataCom("entertainment")
        }
    }
}