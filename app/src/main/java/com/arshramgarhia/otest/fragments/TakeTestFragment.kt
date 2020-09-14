package com.arshramgarhia.otest.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsh.testo.R
import com.arshramgarhia.otest.adapters.TakeTestAdapter
import com.arshramgarhia.otest.dataClasses.QuestionModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_take_test.*
import java.lang.Exception

class TakeTestFragment : Fragment() {
    val navArgs: TakeTestFragmentArgs by navArgs()
    lateinit var cardView: MaterialCardView
    lateinit var txtTitle: MaterialTextView
    lateinit var txtUser: MaterialTextView
    lateinit var txtQuesCount: MaterialTextView
    lateinit var btnStartTest: Button
    lateinit var fabSubmit: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var navController: NavController
    lateinit var testId: String

    var questionList = ArrayList<QuestionModel>()
    var correctOptions = HashMap<String, String>()

    val dbRef = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        val argFromUri = arguments?.getString("id")
        testId = argFromUri ?: navArgs.testId

        Toast.makeText(requireContext(), testId, Toast.LENGTH_LONG).show()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_take_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getTest(testId)
        navController = Navigation.findNavController(view)


        cardView = view.findViewById(R.id.cardTestInfoTakeTest)
        txtTitle = view.findViewById(R.id.txtTitleTakeTest)
        txtUser = view.findViewById(R.id.txtUserTakeTest)
        txtQuesCount = view.findViewById(R.id.txtQuesCountTakeTest)
        btnStartTest = view.findViewById(R.id.startTest)
        fabSubmit = view.findViewById(R.id.fabBtnSubmit)
        recyclerView = view.findViewById(R.id.recyclerTakeTest)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && fabSubmit.isShown) {
                    fabSubmit.hide()
                }
                if (dy < 0 && fabSubmit.isOrWillBeHidden) {
                    fabSubmit.show()
                }
            }
        })

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter =
            TakeTestAdapter(questionList)
        recyclerView.adapter = adapter

        fabSubmit.setOnClickListener {
            val selectedOptions = adapter.getSelected()
            val score = checkScores(selectedOptions)
            registerScoreToDB(score)
            toScores(score)
        }

        btnStartTest.setOnClickListener {
            recyclerView.visibility = View.VISIBLE
            cardView.visibility = View.GONE
            fabSubmit.visibility = View.VISIBLE

        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun getTest(uId: String) {
        Log.i("recievedID", uId)
        questionList.clear()
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val testsObj = snapshot.child("tests").value as HashMap<*, *>
                    val userObj = snapshot.child("users").value as HashMap<*, *>
                    Log.i("testObj", uId)
                    val test = testsObj[uId] as HashMap<*, *>
                    val title = test["title"]
                    val userId = test["created_by"]
                    Log.i("tatti", "$title, $userId")
                    val questions = test["questions"] as ArrayList<*>
                    val questionCount = questions.size
                    questions.mapIndexed { index, it ->
                        val data = it as HashMap<*, *>
                        val body = data["question_body"]
                        val options = data["options"] as HashMap<String, Boolean>
                        options.map { op ->
                            if (op.value) {
                                correctOptions["$index"] = op.key
                            }
                        }
                        val questionModel =
                            QuestionModel(
                                body.toString(),
                                options
                            )
                        questionList.add(questionModel)
                        Log.i("dekhlai", questionList.toString())
                    }
                    val creator = userObj[userId] as HashMap<*,*>
                    val userName = creator["username"] as String
                    txtTitle.text = title.toString()
                    txtUser.text = "By $userName"
                    txtQuesCount.text = "$questionCount Questions"
                    cardView.visibility = View.VISIBLE
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        )
    }

    fun checkScores(selected: HashMap<String, String>): Int {
        var score = 0
        selected.map {
            if (it.value == correctOptions[it.key])
                score += 1
        }
        return score
    }

    fun toScores(score: Int) {
        val action = TakeTestFragmentDirections.actionTakeTestFragmentToScoresFragment(
            score,
            questionList.size
        )
        navController.navigate(action)
    }

    fun registerScoreToDB(score: Int) {
        val database = Firebase.database.reference
        val users = database.child("users")
        val scores = users.child(Firebase.auth.currentUser!!.uid).child("scores")
        scores.child(testId).setValue("$score:${questionList.size}")
    }
}

