package com.arsh.testo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.arsh.testo.R
import com.arsh.testo.dataClasses.TestModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class TakeTestFragment : Fragment() {
    val args: TakeTestFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_take_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val testId = args.testId
        getTest(testId)
        super.onViewCreated(view, savedInstanceState)
    }
    fun getTest(uId: String) {
        Firebase.database.reference.child("tests").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val tests = snapshot.value as HashMap<*,*>
                Log.i("dekhlai", tests[uId].toString())
            }
        }
        )
    }
}