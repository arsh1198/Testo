package com.arshramgarhia.otest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.arsh.testo.R
import com.arshramgarhia.otest.dataClasses.UserModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginSignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signUpContainer: LinearLayout
    private lateinit var btnSwitchMode: Button
    private lateinit var btnSignUp: Button
    private lateinit var txtFormHeading: TextView
    private lateinit var headingContainer: LinearLayout
    private lateinit var txtChangeMode: TextView
    private lateinit var txtEmail: TextInputLayout
    private lateinit var txtUserName: TextInputLayout
    private lateinit var txtPassword: TextInputLayout
    private lateinit var signUpProgressBar: ProgressBar

    private var signUpMode: Boolean = true
    private var database = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_signup_activity)

        auth = Firebase.auth

        ifLoggedIn()

        signUpContainer = findViewById(R.id.SignUpContainer)
        txtFormHeading = findViewById(R.id.txtFormHeading)
        headingContainer = findViewById(R.id.headingContainer)
        txtChangeMode = findViewById(R.id.txtChangeMode)
        btnSwitchMode = findViewById(R.id.btnSwitchMode)
        btnSignUp = findViewById(R.id.btnSignUp)
        txtEmail = findViewById(R.id.txtSignUpEmail)
        txtUserName = findViewById(R.id.txtSignUpUserId)
        txtPassword = findViewById(R.id.txtPassword)
        signUpProgressBar = findViewById(R.id.signUpProgressBar)

        signUpProgressBar.visibility = View.GONE
        switchMode()

        btnSignUp.setOnClickListener {

            val email = txtEmail.editText?.text
            val userName = txtUserName.editText?.text
            val password = txtPassword.editText?.text

            txtEmail.error = null
            txtUserName.error = null
            txtPassword.error = null

            var failFlag = false

            if (email.toString().trim().isEmpty()) {
                failFlag = true
                txtEmail.error = "Email cannot be Empty!"
            }
            if (password.toString().trim().isEmpty()) {
                failFlag = true
                txtPassword.error = "Please enter a Password!"
            }
            if (signUpMode) {
                if (userName.toString().trim().isEmpty()) {
                    failFlag = true
                    txtUserName.error = "Username Required!"
                }
            }
            if (!failFlag) {
                if (signUpMode) {
                    signUp(userName.toString(), email.toString(), password.toString())
                } else {
                    signIn(email.toString(), password.toString())
                }
            }
        }

        btnSwitchMode.setOnClickListener {
            switchMode()
        }
    }

    private fun signUp(username: String, email: String, password: String) {
        signUpProgressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->

            if (task.isSuccessful) {
                val user = auth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = username
                }

                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { t ->
                        if (t.isSuccessful) {
                            registerUserToDb(user)
                            Log.d("pliss", "User profile updated.")
                        }
                    }
                user.sendEmailVerification().addOnCompleteListener { task2 ->
                    if (task2.isSuccessful) {
                        signUpProgressBar.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "A verification email has been sent, verify your email to Log in.",
                            Toast.LENGTH_LONG
                        ).show()
                        switchMode()
                    } else {
                        Toast.makeText(this, task2.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                signUpProgressBar.visibility = View.GONE
                Toast.makeText(this@LoginSignUpActivity, task.exception?.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun signIn(email: String, password: String) {
        signUpProgressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user!!.isEmailVerified) {
                    toNextActivity()
                    Toast.makeText(
                        this,
                        "Logged In as ${user.email}",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    signUpProgressBar.visibility = View.GONE
                    auth.signOut()
                    Toast.makeText(
                        this,
                        "Please verify your email!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                signUpProgressBar.visibility = View.GONE
                Toast.makeText(
                    this@LoginSignUpActivity,
                    task.exception?.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun switchMode() {

        txtEmail.error = null
        txtUserName.error = null
        txtPassword.error = null

        if (signUpMode) {
            signUpMode = false
            signUpContainer.removeViewAt(2)
            changeStrings(
                R.string.txtSign_Up,
                R.string.txtSign_In
            )
        } else {
            signUpMode = true
            signUpContainer.addView(txtUserName, 2)
            changeStrings(
                R.string.txtSign_In,
                R.string.txtSign_Up
            )
        }
    }

    private fun changeStrings(fromId: Int, toId: Int) {
        btnSignUp.text = getString(toId)
        btnSwitchMode.text = getString(fromId)
        signUpContainer.removeViewAt(0)
        txtFormHeading.text = getString(toId)
        signUpContainer.addView(headingContainer, 0)
        when (toId) {
            R.string.txtSign_In -> txtChangeMode.text = getString(
                R.string.new_here
            )
            R.string.txtSign_Up -> txtChangeMode.text = getString(
                R.string.already_a_member
            )
        }
    }

    private fun registerUserToDb(user: FirebaseUser) {
        database.child("users").child(user.uid)
            .setValue(
                UserModel(
                    user.displayName.toString(),
                    user.email.toString()
                )
            )
    }
    private fun ifLoggedIn(){
        if (auth.currentUser != null){
            toNextActivity()
        }
    }

    private fun toNextActivity(){
        intent = Intent(this@LoginSignUpActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}


