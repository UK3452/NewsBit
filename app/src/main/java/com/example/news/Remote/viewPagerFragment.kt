package com.example.news.Remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.news.Adapter.ViewPagerAdapter
import com.example.news.R
import com.example.news.Activity.Screen1
import com.example.news.Activity.Screen2
import com.example.news.Activity.Screen3

class viewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        Handler().postDelayed(
//                {
//                    findNavController().navigate(R.id.action_mainActivity_to_screen1)
//                },3000)
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
                Screen1(),
                Screen2(),
                Screen3()
        )

        val adapter = ViewPagerAdapter(fragmentList,
        requireActivity().supportFragmentManager,
                lifecycle
        )
        val viewPager1 = activity?.findViewById<ViewPager2>(R.id.view_pager)
        if (viewPager1 != null) {
            viewPager1.adapter = adapter
        }
        return view
    }

}