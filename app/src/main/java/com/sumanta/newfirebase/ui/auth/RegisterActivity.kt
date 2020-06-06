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
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        button_register.setOnClickListener {
            val email = edit_text_email_r.text.toString().trim()
            val password = edit_text_password_r.text.toString().trim()


            if (email.isEmpty()){
                edit_text_email_r.error = "Email Required"
                edit_text_email_r.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email_r.error = "Valid Email Required"
                edit_text_email_r.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length<6){
                edit_text_password_r.error = "6 char password required"
                edit_text_password_r.requestFocus()
                return@setOnClickListener
            }

            registerUser(email, password)

        }


        text_view_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun registerUser(email: String, password: String) {
        progressbar.show()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                progressbar.hide()
                if (task.isSuccessful){

                    val intent = Intent(this@RegisterActivity, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }

                    startActivity(intent)

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