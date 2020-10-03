package com.arshramgarhia.otest.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.arsh.testo.R
import com.arshramgarhia.otest.dataClasses.TestModel
import com.arshramgarhia.otest.fragments.MyTestsFragmentDirections
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.scores_single_row.view.*

class MyTestsAdapter(val testList: ArrayList<TestModel>) :
    RecyclerView.Adapter<MyTestsAdapter.ViewHolder>() {
    val database = Firebase.database.reference
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val btnCheckScores = itemView.findViewById<Button>(R.id.btnCheckScores)
        val txtTtile = itemView.findViewById<MaterialTextView>(R.id.txtTitleScore)
        val txtMadeBy = itemView.findViewById<MaterialTextView>(R.id.txtUserScore)
        val txtQuesCount = itemView.findViewById<MaterialTextView>(R.id.txtQuesCountScore)
        val txtResponses = itemView.findViewById<MaterialTextView>(R.id.txtRecyclerScoreAndResponse)
        val txtResponsesText = itemView.findViewById<MaterialTextView>(R.id.txtResponseText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.scores_single_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checkScore = holder.btnCheckScores
        checkScore.visibility = View.VISIBLE
        checkScore.setOnClickListener{
                view ->
            val action = MyTestsFragmentDirections.actionMiTestsToCheckScoresFragment(testList[position].Uid)
            view.findNavController().navigate(action)
        }
        holder.txtMadeBy.visibility = View.GONE
        holder.txtResponses.visibility = View.GONE
        holder.txtResponsesText.visibility = View.VISIBLE
        holder.txtResponsesText.text = "${testList[position].score} Responses"
        holder.txtTtile.text = "${testList[position].title}"
        holder.txtQuesCount.text = "${testList[position].questions.size} Questions"
    }

    override fun getItemCount(): Int {
        return testList.size
    }
}