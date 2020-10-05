package com.arshramgarhia.otest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsh.testo.R
import com.arshramgarhia.otest.adapters.MyTestsAdapter
import com.arshramgarhia.otest.adapters.ScoresAdapter
import com.arshramgarhia.otest.dataClasses.QuestionModel
import com.arshramgarhia.otest.dataClasses.TestModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyTestsFragment : Fragment() {
    val user = Firebase.auth.currentUser
    val database = Firebase.database.reference
    lateinit var adapter: MyTestsAdapter
    var testList = arrayListOf<TestModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_tests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testList.clear()

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val testObj = snapshot.child("tests").value as HashMap<*, *>
                val users = snapshot.child("users").value as HashMap<*,*>
                val Uid = user?.uid

                testObj.map { obj ->
                    var responses = 0
                    val tests = obj.value as HashMap<*,*>
                    val title = tests["title"] as String
                    val createdBy = tests["created_by"] as String
                    val questions = tests["questions"] as MutableList<QuestionModel>
                    val testId = tests["uid"] as String
                    val currUser = users["$Uid"] as HashMap<*,*>
                    if(createdBy == Uid){
                        users.map { userObj ->
                            val user = userObj.value as HashMap<*, *>
                            if(user["scores"]!=null){
                                val scores = user["scores"] as HashMap<*, *>
                                scores.map {
                                    if(it.key == testId){
                                        responses+=1
                                    }
                                }
                            }
                        }
                        val test = TestModel(testId, title, "username", questions, responses.toString())
                        testList.add(test)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMyTests)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = MyTestsAdapter(testList)
        recyclerView.adapter = adapter
    }
}