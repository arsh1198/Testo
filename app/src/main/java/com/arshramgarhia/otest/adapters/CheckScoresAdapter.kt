package com.arshramgarhia.otest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.arsh.testo.R
import com.arshramgarhia.otest.dataClasses.QuestionModel
import kotlinx.android.synthetic.main.take_test_recycler_row.view.*
import org.w3c.dom.Text

class CheckScoresAdapter(val itemList: ArrayList<Pair<String, Char>>): RecyclerView.Adapter<CheckScoresAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtTitle = itemView.findViewById<TextView>(R.id.txtTitleScore)
        val txtMadeBy = itemView.findViewById<TextView>(R.id.txtUserScore)
        val txtQuestionCount = itemView.findViewById<TextView>(R.id.txtQuesCountScore)
        val txtScore = itemView.findViewById<TextView>(R.id.txtRecyclerScoreAndResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.scores_single_row, parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTitle.text = itemList[position].first
        holder.txtScore.text = itemList[position].second.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}