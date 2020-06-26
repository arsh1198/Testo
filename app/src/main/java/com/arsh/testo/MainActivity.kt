package com.arsh.testo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var btnSignUp: Button
    lateinit var txtEmail: TextInputLayout
    lateinit var txtUserName: TextInputLayout
    lateinit var txtPassword: TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        btnSignUp = findViewById(R.id.btnSignUp)
        txtEmail = findViewById(R.id.txtSignUpEmail)
        txtUserName = findViewById(R.id.txtSignUpUserId)
        txtPassword = findViewById(R.id.txtPassword)

        val email = txtEmail.editText?.text
        val password = txtPassword.editText?.text

        btnSignUp.setOnClickListener {
            signUp(email.toString(), password.toString())
        }


    }

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                auth.currentUser!!.sendEmailVerification().addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "A verification email has been sent, verify your email to Log in.", Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_LONG).show()
            }
        }
    }
}