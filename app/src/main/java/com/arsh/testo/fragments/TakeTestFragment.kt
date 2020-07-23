package com.arsh.testo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.arsh.testo.R
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import kotlin.collections.HashMap

class TakeTestFragment : Fragment() {
    val args: TakeTestFragmentArgs by navArgs()
    lateinit var txtTitle: MaterialTextView
    lateinit var txtUser: MaterialTextView
    lateinit var txtQuesCount: MaterialTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_take_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val testId = args.testId
        txtTitle = view.findViewById(R.id.txtTitleTakeTest)
        txtUser = view.findViewById(R.id.txtUserTakeTest)
        txtQuesCount = view.findViewById(R.id.txtQuesCountTakeTest)

        getTest(testId)
        super.onViewCreated(view, savedInstanceState)
    }

    fun getTest(uId: String) {
        val dbRef = Firebase.database.reference
        dbRef.child("tests").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val testsObj = snapshot.value as HashMap<*, *>
                val test = testsObj[uId] as HashMap<*, *>
                val title = test["title"]
                val userId = test["created_by"]
                Log.i("tatti", "$title, $userId")
                val questionsList = test["questions"] as ArrayList<*>
                val questionCount = questionsList.size
                dbRef.child("users").addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val usersObj = snapshot.value as HashMap<*, *>
                        val user = usersObj[userId] as HashMap<*, *>
                        val username = user["username"]
                        txtTitle.text = title.toString()
                        txtUser.text = "By $username"
                        txtQuesCount.text = "$questionCount Questions"
                    }
                })

            }
        }
        )
    }
}