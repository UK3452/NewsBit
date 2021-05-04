package com.example.news.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.news.R
import kotlinx.android.synthetic.main.activity_onboarding_fragment.*

class OnboardingFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var isVisible : Boolean = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_fragment)
        supportFragmentManager.commit {
            hide(fragment_one)
        }

        ctrlBtn.setOnClickListener {
            if(isVisible){
                supportFragmentManager.commit {
                    isVisible = false
                    hide(fragment_one)
                }
            }
            else{
                supportFragmentManager.commit {
                    isVisible = true
                    show(fragment_one)
                }
            }
        }
    }
}