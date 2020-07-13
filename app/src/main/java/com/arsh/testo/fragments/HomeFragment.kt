package com.arsh.testo.fragments

import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.arsh.testo.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.zip.Inflater

class HomeFragment : Fragment(), View.OnClickListener {

    lateinit var navController: NavController

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
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnMakeTest -> createInputDialogue("Enter a title for your Test", "kuch bhi")
            R.id.btnTakeTest -> navController.navigate(R.id.action_homeFragment_to_takeTestFragment)
        }
    }

    fun createInputDialogue(title: String, message: String) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setView(layoutInflater.inflate(R.layout.input_dialog, null))
            .setTitle(title)
            .setNeutralButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Ok") { dialog, which -> }
            .show()
    }
}