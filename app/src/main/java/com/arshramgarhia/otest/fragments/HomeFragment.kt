package com.arshramgarhia.otest.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.arsh.testo.R
import com.arshramgarhia.otest.activities.LoginSignUpActivity
import com.arshramgarhia.otest.fragments.HomeFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var navController: NavController
    lateinit var txtInputTitle: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnMakeTest).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnTakeTest).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnMakeTest -> {
                navigateToMakeTestWithDialog(
                    "Title",
                    "Enter a Title Please!",
                    "A suitable title for your test"
                )
            }
            R.id.btnTakeTest -> navigateToTakeTestWithDialog(
                "Unique Id",
                "Enter to take a test!",
                " The Id is used to identify a test"
            )
            R.id.btnLogout -> {
                Firebase.auth.signOut()
                val intent = Intent(activity, LoginSignUpActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    fun navigateToMakeTestWithDialog(title: String, hint: String, helpText: String) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.input_dialog, null)
        txtInputTitle = dialogLayout.findViewById(R.id.txtInputDialog)
        txtInputTitle.hint = hint
        txtInputTitle.helperText = helpText
        val testTitle = txtInputTitle.editText?.text
        builder.setView(dialogLayout)
            .setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.setPositiveButton("Ok") { dialog, which ->
            val action =
                HomeFragmentDirections.actionHomeFragmentToCreateTestFragment(
                    testTitle.toString()
                )
            navController.navigate(action)
        }.setCancelable(false)
        builder.show()
    }

    fun navigateToTakeTestWithDialog(title: String, hint: String, helpText: String) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.input_dialog, null)
        txtInputTitle = dialogLayout.findViewById(R.id.txtInputDialog)
        txtInputTitle.hint = hint
        txtInputTitle.helperText = helpText
        val uId = txtInputTitle.editText?.text
        builder.setView(dialogLayout)
            .setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.setPositiveButton("Ok") { dialog, which ->
            val action =
                HomeFragmentDirections.actionHomeFragmentToTakeTestFragment(
                    uId.toString()
                )
            navController.navigate(action)
        }.setCancelable(false)
        builder.show()
    }
}

