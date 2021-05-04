package com.example.news.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.news.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.concurrent.TimeUnit

class SignInActivity : AppCompatActivity() {
    companion object{
        private const val RC_SIGN_IN = 120
    }
    private var codeSent : String=""
//    private lateinit var tokenID: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mAuth:FirebaseAuth
    private lateinit var googleSignInClient:GoogleSignInClient
    private var phoneNo : String=""
    override fun onCreate(savedInstanceState: Bundle?) {

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController

            if(controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        ccp.registerPhoneNumberTextView(otp_input)
        ccp.showFullName(true)
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth= FirebaseAuth.getInstance()
        google_login_btn.setOnClickListener{
            signIn()
        }
        verifyBtn.setOnClickListener { signwithPhoneNumber() }
        // Callback function for Phone Auth

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // verification completed
                Toast.makeText(applicationContext,"OTP SENT!!",Toast.LENGTH_SHORT).show()
                var intent = Intent(applicationContext, VerifyGenerateOTP::class.java)
                intent.putExtra("otpCode",codeSent)
                intent.putExtra("phoneNo",phoneNo)
                startActivity(intent)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked if an invalid request for verification is made,
                // for instance if the the phone number format is invalid.
                Toast.makeText(applicationContext,"Login Failed!!",Toast.LENGTH_SHORT).show()

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(applicationContext,"INVALID PHONE NUMBER",Toast.LENGTH_SHORT).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(applicationContext,"NO OF REQUEST EXCEEDED",Toast.LENGTH_SHORT).show()
                }

            }
            override fun onCodeSent(verificationId: String?,
                                    token: PhoneAuthProvider.ForceResendingToken?) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
//                showSnackbar("onCodeSent:" + verificationId)
                // Save verification ID and resending token so we can use them later
                codeSent = verificationId.toString()
//                tokenID = token!!
            }
            override fun onCodeAutoRetrievalTimeOut(verificationId: String?) {
                // called when the timeout duration has passed without triggering onVerificationCompleted
                super.onCodeAutoRetrievalTimeOut(verificationId)
            }
        }

    }

    private fun checkValidPhnNumber(): Boolean {
        var phoneNumber = otp_input.text.toString().trim()
        if(!(phoneNumber.isEmpty() || phoneNumber=="" || phoneNumber.length != 10))
        {
            phone_num.error=""
            return true
        }
        Toast.makeText(this,"Enter Valid Phone Number",Toast.LENGTH_SHORT).show()
        phone_num.setError("Enter Valid Phone Number")
        phone_num.requestFocus()
        return false
    }


    private fun signwithPhoneNumber(){
        if(checkValidPhnNumber()){
            phoneNo=ccp.fullNumberWithPlus
            var number =phoneNo.toString().trim()
            intent.putExtra("phoneNo", phoneNo)
            sendVerificationcode(number)
        }
    }
    private fun sendVerificationcode(phoneNumber: String) {

        val options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(20, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            }
            else{
                Log.w("SignInActivity", exception.toString())
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("SignInActivity", "signInWithCredential:success")
                        val intent = Intent(this, Dashboard::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("SignInActivity", "signInWithCredential:failure", task.exception)
                    }
                }
    }
}

