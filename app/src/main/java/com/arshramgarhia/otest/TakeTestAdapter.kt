package com.arshramgarhia.otest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsh.testo.R
import com.arshramgarhia.otest.dataClasses.QuestionModel
import com.google.android.material.textview.MaterialTextView

class TakeTestAdapter(val questionList: ArrayList<QuestionModel>) :
    RecyclerView.Adapter<TakeTestAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtQuesNumber = itemView.findViewById<MaterialTextView>(R.id.txtQuesNumber)
        val txtQuesBody = itemView.findViewById<MaterialTextView>(R.id.txtQuesBodyTakeTest)
        val radioGroup = itemView.findViewById<RadioGroup>(R.id.radioGroupTakeTest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.take_test_recycler_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quesNum = position + 1
        holder.txtQuesNumber.text = "#".plus(quesNum)
        holder.txtQuesBody.text = questionList[position].question_body
        val radioGroup = holder.radioGroup
        val options = questionList[position].options
        var i = 0
        for ((k, v) in options) {
            val radioButton = radioGroup.getChildAt(i) as RadioButton
            radioButton.text = k
            i++
        }
        if (options.size == 2) {
            radioGroup.removeViews(2, 2)
        }
    }
}