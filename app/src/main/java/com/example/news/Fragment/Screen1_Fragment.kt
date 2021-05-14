package com.example.news.Fragment

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
        communicator.passDataCom("Business")
        //Passing data to Dashboard.kt's function
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
            communicator.passDataCom("Health")
        }
        sportsBtn.setOnClickListener {
            communicator = activity as Communicator
            communicator.passDataCom("Sports")
        }
        techBtn.setOnClickListener {
            communicator = activity as Communicator
            communicator.passDataCom("Technology")
        }
        scienceBtn.setOnClickListener {
            communicator = activity as Communicator
            communicator.passDataCom("Science")
        }
        entertainmentBtn.setOnClickListener {
            communicator = activity as Communicator
            communicator.passDataCom("Entertainment")
        }
    }
}