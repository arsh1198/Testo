package com.arshramgarhia.otest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.arsh.testo.R
import com.google.android.material.textview.MaterialTextView

class ScoresFragment : Fragment() {
    val args: ScoresFragmentArgs by navArgs()

         override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val txtScore = view.findViewById<MaterialTextView>(R.id.txtScore)
        txtScore.text = "${args.score}/${args.totalQues}"
    }
}