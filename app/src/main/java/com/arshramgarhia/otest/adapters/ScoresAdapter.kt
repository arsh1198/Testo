package com.arshramgarhia.otest.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsh.testo.R
import com.arshramgarhia.otest.dataClasses.TestModel
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlin.system.measureNanoTime

class ScoresAdapter(val testList: ArrayList<TestModel>): RecyclerView.Adapter<ScoresAdapter.ViewHolder>(){
    val databse = Firebase.database.reference
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtTtile = itemView.findViewById<MaterialTextView>(R.id.txtTitleScore)
        val txtMadeBy = itemView.findViewById<MaterialTextView>(R.id.txtUserScore)
        val txtQuesCount = itemView.findViewById<MaterialTextView>(R.id.txtQuesCountScore)
        val txtScore = itemView.findViewById<MaterialTextView>(R.id.txtRecyclerScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.scores_single_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  testList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val test = testList[position]
        holder.txtTtile.text = test.title
        holder.txtMadeBy.text = test.created_by
        holder.txtQuesCount.text = test.questions.size.toString()
        val arr = extractScores(test.score)
        val score = if (arr[0].isNotEmpty()) "${arr[0]}/${arr[1]}" else "Not Attempted yet"
        holder.txtScore.text = score
    }

    fun extractScores(string : String): List<String>{
        return string.split(":")
    }
}