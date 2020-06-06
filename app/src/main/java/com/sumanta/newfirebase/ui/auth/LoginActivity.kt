package com.sumanta.newfirebase.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.sumanta.newfirebase.R
import com.sumanta.newfirebase.ui.home.HomeActivity
import com.sumanta.newfirebase.util.hide
import com.sumanta.newfirebase.util.login
import com.sumanta.newfirebase.util.show
import com.sumanta.newfirebase.util.toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()


        button_sign_in.setOnClickListener {
            val email = edit_text_email_l.text.toString().trim()
            val password = edit_text_password_l.text.toString().trim()


            if (email.isEmpty()){
                edit_text_email_l.error = "Email Required"
                edit_text_email_l.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email_l.error = "Valid Email Required"
                edit_text_email_l.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length<6){
                edit_text_password_l.error = "6 char password required"
                edit_text_password_l.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)

        }


        text_view_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        progressbar.show()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                progressbar.hide()
                if (task.isSuccessful){
                login()
                }else{
                    task.exception?.message?.let {
                        toast(it)
                    }
                }

            }

    }

    override fun onStart() {
        super.onStart()

        mAuth.currentUser?.let {
            login()
        }
    }

}