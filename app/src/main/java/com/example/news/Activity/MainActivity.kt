package com.example.news.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.news.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private  lateinit  var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if(controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val topanim =AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnim =AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        val startAnim = AnimationUtils.loadAnimation(this, R.anim.start_anim)
        logoTxt1.startAnimation(topanim)
        logo_img.startAnimation(topanim)
        slogan1.startAnimation(bottomAnim)
        slogan2.startAnimation(bottomAnim)
        mAuth = FirebaseAuth.getInstance()
        val user=mAuth.currentUser

        Handler().postDelayed({
            mAuth.addAuthStateListener { firebaseAuth ->
                if (firebaseAuth.currentUser == null || !firebaseAuth.currentUser!!.isEmailVerified){
                    val signInIntent = Intent(this, SignInActivity::class.java)
                    startActivity(signInIntent)
                    finish()
                }
                //User not logger or email not verified
                else {
//                    val currentUser = firebaseAuth.currentUser!!
                    val dashboardIntent = Intent(this, Dashboard::class.java)
                    startActivity(dashboardIntent)
                    finish()
                }
            }
        },3000)
    }

}