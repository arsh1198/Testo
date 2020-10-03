package com.arshramgarhia.otest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavArgs
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsh.testo.R
import com.arshramgarhia.otest.adapters.CheckScoresAdapter
import com.arshramgarhia.otest.adapters.MyTestsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CheckScoresFragment : Fragment() {
    val navArgs: CheckScoresFragmentArgs by navArgs()
    val database = Firebase.database.reference
    lateinit var adapter: CheckScoresAdapter
    var scoreList = ArrayList<Pair<String, Char>>()
    lateinit var testId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testId = navArgs.testID
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_scores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        scoreList.clear()
        super.onViewCreated(view, savedInstanceState)

        database.addValueEventListener(object : ValueEventListener   {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.child("users").value as HashMap<*, *>
                users.map { userObj ->
                    val user = userObj.value as HashMap<*, *>
                    if(user["scores"]!= null){
                        val scores = user["scores"] as HashMap<*, *>
                        scores.map {
                            if(it.key == testId){
                                val score = scores[testId].toString().trim(':')
                                scoreList.add(Pair(userObj.key.toString(),score[0]))
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerCheckScore)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = CheckScoresAdapter(scoreList)
        recyclerView.adapter = adapter
    }
}