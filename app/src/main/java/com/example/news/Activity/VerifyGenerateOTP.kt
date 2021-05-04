package com.example.news.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.example.news.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_verify_generate_o_t_p.*
import java.util.concurrent.TimeUnit


class VerifyGenerateOTP : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var credential : PhoneAuthCredential
    private var verifyCode : String=""
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val mUser: FirebaseUser = auth.getCurrentUser()
        setContentView(R.layout.activity_verify_generate_o_t_p)
        val codeSent=intent.getStringExtra("otpCode")
//        token = intent.getStringExtra("token").toString()
        var phoneNo = intent.extras?.get("phoneNo")
        var fetchcode:String = ""
        otp_msg.setText("OTP has been sent to " + phoneNo.toString() + " please enter it below")
        startOTPTimer()
        verifyBtn.setOnClickListener{
            var code = otp_input.text.toString().trim()
            try {
                if(code.isEmpty()){
                    otp_input.setError("Enter 6 Digit OTP")
                }
                else if(!(code.length==6)){
                    otp_input.setError("Invalid OTP")
                }
                else{
                    credential = PhoneAuthProvider.getCredential(codeSent!!, code)
                    signInWithPhoneAuthCredential(credential)
                }
            }
            catch (e: Exception){
                Log.e("VerifyException", e.toString())
            }
        }

        resend_Btn.setOnClickListener {
            startOTPTimer()
            resendOTP(phoneNo.toString().trim())
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // verification completed
                Toast.makeText(applicationContext, "OTP SENT!!", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked if an invalid request for verification is made,
                // for instance if the the phone number format is invalid.
                Toast.makeText(applicationContext, "Login Failed!!", Toast.LENGTH_SHORT).show()

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(applicationContext, "INVALID PHONE NUMBER", Toast.LENGTH_SHORT).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(applicationContext, "NO OF REQUEST EXCEEDED", Toast.LENGTH_SHORT).show()
                }

            }
            override fun onCodeSent(
                verificationId: String?,
                token: PhoneAuthProvider.ForceResendingToken?
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
//                showSnackbar("onCodeSent:" + verificationId)
                // Save verification ID and resending token so we can use them later
                verifyCode = verificationId.toString()
//                tokenID = token!!
            }
            override fun onCodeAutoRetrievalTimeOut(verificationId: String?) {
                // called when the timeout duration has passed without triggering onVerificationCompleted
                super.onCodeAutoRetrievalTimeOut(verificationId)
            }
        }

    }

    private fun startOTPTimer() {
        resend_Btn.isClickable = false
        resend_Btn.linksClickable = false
        val timer = object: CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
//                val minutes = millisUntilFinished / 1000 / 60
//                val seconds = millisUntilFinished / 1000 % 60

                val hms = String.format(
                    "%02d:%02d",
                    (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                            TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(
                                    millisUntilFinished
                                )
                            )),
                    (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    ))
                )
                resend_Btn.text = "Try Again in "+hms
            }

            override fun onFinish() {
                resend_Btn.isClickable = true
                resend_Btn.linksClickable = true
                resend_Btn.text = "Resend OTP"
                resend_Btn.setTextColor(Color.parseColor("#FFFFFFFF"))
                resend_Btn.setPadding(20)
                resend_Btn.isEnabled = true
                cancel()
            }
        }
        timer.start()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            applicationContext,
                            "Sign In Successful!!",
                            Toast.LENGTH_LONG
                        ).show()
                        var intent = Intent(applicationContext, Dashboard::class.java)
                        startActivity(intent)
                        finish()
                        val user = task.result?.user
                        auth.updateCurrentUser(user)
                    } else {
                        // Sign in failed, display a message and update the UI
//                        Toast.makeText(applicationContext,"Incorrect OTP, Try Again...",Toast.LENGTH_LONG).show()
                        otp_input.setError("Incorrect OTP!!")
                        Log.w("TAG", "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                        // Update UI
                    }
                }
    }


    private  fun resendOTP(phoneNumber: String){
        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        resend_Btn.setTextColor(Color.parseColor("#FFE927"))
//        resend_Btn.setBackgroundColor(Color.parseColor("#FFE927"))
//        Toast.makeText(applicationContext,"OTP Sent",Toast.LENGTH_LONG).show()
    }
}