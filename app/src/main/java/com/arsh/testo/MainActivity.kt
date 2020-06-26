package com.arsh.testo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var signUpContainer: LinearLayout
    lateinit var btnSwitchMode: Button
    lateinit var btnSignUp: Button
    lateinit var txtFormHeading: TextView
    lateinit var txtChangeMode: TextView
    lateinit var txtEmail: TextInputLayout
    lateinit var txtUserName: TextInputLayout
    lateinit var txtPassword: TextInputLayout

    var signUpMode: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        signUpContainer = findViewById(R.id.SignUpContainer)
        txtFormHeading = findViewById(R.id.txtFormHeading)
        txtChangeMode = findViewById(R.id.txtChangeMode)
        btnSwitchMode = findViewById(R.id.btnSwitchMode)
        btnSignUp = findViewById(R.id.btnSignUp)
        txtEmail = findViewById(R.id.txtSignUpEmail)
        txtUserName = findViewById(R.id.txtSignUpUserId)
        txtPassword = findViewById(R.id.txtPassword)

        val email = txtEmail.editText?.text
        val password = txtPassword.editText?.text

        btnSignUp.setOnClickListener {
            signUp(email.toString(), password.toString())
        }

        btnSwitchMode.setOnClickListener {
            switchMode()
        }
    }

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                auth.currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "A verification email has been sent, verify your email to Log in.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun switchMode() {
        if (signUpMode) {
            signUpMode = false
            signUpContainer.removeViewAt(2)
            changeStrings(R.string.txtSign_Up, R.string.txtSign_In)

        } else {
            signUpMode = true
            signUpContainer.addView(txtUserName, 2)
            changeStrings(R.string.txtSign_In, R.string.txtSign_Up)
        }
    }

    fun changeStrings(from: Int, to: Int) {
        btnSignUp.text = getString(to)
        btnSwitchMode.text = getString(from)
        signUpContainer.removeViewAt(0)
        txtFormHeading.text = getString(to)
        signUpContainer.addView(txtFormHeading, 0)
        when (to) {
            R.string.txtSign_In -> txtChangeMode.text = getString(R.string.new_here)
            R.string.txtSign_Up -> txtChangeMode.text = getString(R.string.already_a_member)
        }
    }
}

