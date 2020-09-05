package com.arshramgarhia.otest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsh.testo.R
import com.arshramgarhia.otest.dataClasses.QuestionModel
import com.google.android.material.textview.MaterialTextView

class TakeTestAdapter(val questionList: ArrayList<QuestionModel>) :
    RecyclerView.Adapter<TakeTestAdapter.ViewHolder>() {

    var correctOptions = hashMapOf<String,String>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container = itemView.findViewById<LinearLayout>(R.id.singleRowContainer)
        val txtQuesNumber = itemView.findViewById<MaterialTextView>(R.id.txtQuesNumber)
        val txtQuesBody = itemView.findViewById<MaterialTextView>(R.id.txtQuesBodyTakeTest)
        val radioGroup = itemView.findViewById<RadioGroup>(R.id.radioGroupTakeTest)
        val rbFirst = itemView.findViewById<RadioButton>(R.id.rbFirst)
        val rbSecond = itemView.findViewById<RadioButton>(R.id.rbSecond)
        val rbThird = itemView.findViewById<RadioButton>(R.id.rbThird)
        val rbFourth = itemView.findViewById<RadioButton>(R.id.rbFourth)

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
            for(j in radioGroup.childCount-1 downTo 2){
                radioGroup.getChildAt(j).visibility = View.GONE
            }
        }

        holder.rbFirst.setOnClickListener {
            correctOptions["$position"] = holder.rbFirst.text.toString()
        }
        holder.rbSecond.setOnClickListener {
            correctOptions["$position"] = holder.rbSecond.text.toString()
        }
        holder.rbThird.setOnClickListener {
            correctOptions["$position"] = holder.rbThird.text.toString()
        }
        holder.rbFourth.setOnClickListener {
            correctOptions["$position"] = holder.rbFourth.text.toString()
        }
    }

    fun getSelected() : HashMap<String, String>{
        return correctOptions
    }
}