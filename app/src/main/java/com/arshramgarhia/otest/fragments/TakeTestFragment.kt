package com.arshramgarhia.otest.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsh.testo.R
import com.arshramgarhia.otest.TakeTestAdapter
import com.arshramgarhia.otest.dataClasses.QuestionModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_take_test.*
import kotlin.collections.HashMap

class TakeTestFragment : Fragment() {
    val args: TakeTestFragmentArgs by navArgs()
    lateinit var cardView: MaterialCardView
    lateinit var txtTitle: MaterialTextView
    lateinit var txtUser: MaterialTextView
    lateinit var txtQuesCount: MaterialTextView
    lateinit var btnStartTest: Button
    lateinit var recyclerView: RecyclerView

    var questionList = ArrayList<QuestionModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_take_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val testId = args.testId
        cardView = view.findViewById(R.id.cardTestInfoTakeTest)
        txtTitle = view.findViewById(R.id.txtTitleTakeTest)
        txtUser = view.findViewById(R.id.txtUserTakeTest)
        txtQuesCount = view.findViewById(R.id.txtQuesCountTakeTest)

        btnStartTest = view.findViewById(R.id.startTest)
        recyclerView = view.findViewById(R.id.recyclerTakeTest)

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = TakeTestAdapter(questionList)
        recyclerView.adapter = adapter

        btnStartTest.setOnClickListener {
            /*adapter.notifyDataSetChanged()*/
            recyclerTakeTest.visibility = View.VISIBLE
            cardView.visibility = View.GONE
        }
        if(testId != "blank")
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
                val questions = test["questions"] as ArrayList<*>
                val questionCount = questions.size
                questions.map {
                    val data = it as HashMap<*, *>
                    val body = data["question_body"]
                    val options = data["options"] as HashMap<String, Boolean>
                    val questionModel =
                        QuestionModel(
                            body.toString(),
                            options
                        )
                    questionList.add(questionModel)
                    Log.i("dekhlai", questionList.toString())
                }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val id = arguments?.getString("id")
        if (id != null) {
            getTest(id.toString())
            Toast.makeText(requireContext(), id, Toast.LENGTH_LONG).show()
        }
    }
}