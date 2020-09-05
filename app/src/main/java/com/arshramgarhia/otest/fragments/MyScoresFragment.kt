package com.arshramgarhia.otest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsh.testo.R
import com.arshramgarhia.otest.adapters.ScoresAdapter
import com.arshramgarhia.otest.dataClasses.QuestionModel
import com.arshramgarhia.otest.dataClasses.TestModel
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyScoresFragment : Fragment() {
    val user = Firebase.auth.currentUser
    val database = Firebase.database.reference
    var testList = arrayListOf<TestModel>()
    lateinit var adapter: ScoresAdapter
    lateinit var txtNoScore: MaterialTextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_scores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtNoScore = view.findViewById(R.id.txtNoScore)

        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val testObj = snapshot.child("tests").value as HashMap<*, *>
                val users = snapshot.child("users").value as HashMap<*,*>
                testObj.map {
                    val tests = it.value as HashMap<*,*>
                    val title = tests["title"] as String
                    val createdBy = tests["created_by"] as String
                    val questions = tests["questions"] as MutableList<QuestionModel>
                    val testId = tests["uid"] as String
                    val currUser = users["${user?.uid}"] as HashMap<*,*>
                    val creator = users[createdBy] as HashMap<*,*>
                    val userName = creator["username"] as String
                    if(currUser["scores"] != null){
                        txtNoScore.visibility = View.GONE
                        val scores = currUser["scores"] as HashMap<*,*>
                        if (scores[testId] != null){
                            val currScore = scores[testId] as String
                            val test = TestModel(testId, title, userName, questions, currScore)
                            testList.add(test)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
        Log.i("padd", testList.toString())
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerScores)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ScoresAdapter(testList)
        recyclerView.adapter = adapter

    }
}