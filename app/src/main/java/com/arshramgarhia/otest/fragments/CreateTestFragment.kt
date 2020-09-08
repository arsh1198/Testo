package com.arshramgarhia.otest.fragments

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.RadioGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.arsh.testo.R
import com.arshramgarhia.otest.dataClasses.QuestionModel
import com.arshramgarhia.otest.dataClasses.TestModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class CreateTestFragment : Fragment(), View.OnClickListener {
    lateinit var txtQuestionBody: TextInputLayout
    lateinit var txtOptionsCount: MaterialTextView
    lateinit var btnIncreaseOptions: MaterialButton
    lateinit var btnDecreaseOptions: MaterialButton
    lateinit var txtOptions: TextInputLayout
    lateinit var btnAddOptions: MaterialButton
    lateinit var radioGroup: RadioGroup
    lateinit var btnNextQues: MaterialButton
    lateinit var btnPrevQues: MaterialButton
    val defaultOptionsCount = 4
    var questionList = mutableListOf<QuestionModel>()
    val args: CreateTestFragmentArgs by navArgs()
    lateinit var title: String
    lateinit var testID: String
    lateinit var viewView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewView = view
        title = args.questtionTitle
        txtQuestionBody = view.findViewById(R.id.txtQuesBody)
        txtOptionsCount = view.findViewById(R.id.txtOptionsCount)
        btnIncreaseOptions = view.findViewById(R.id.btnIncreaseOptions)
        btnDecreaseOptions = view.findViewById(R.id.btnDecreaseOptions)
        txtOptions = view.findViewById(R.id.txtOptions)
        btnAddOptions = view.findViewById(R.id.btnAddOptions)
        btnNextQues = view.findViewById(R.id.btnNextQues)
        btnPrevQues = view.findViewById(R.id.btnPrevQues)

        txtOptionsCount.text = defaultOptionsCount.toString()
        btnIncreaseOptions.isEnabled = false

        radioGroup = view.findViewById(R.id.rgCreate)

        btnAddOptions.setOnClickListener(this)
        btnIncreaseOptions.setOnClickListener(this)
        btnDecreaseOptions.setOnClickListener(this)
        btnNextQues.setOnClickListener(this)
        btnPrevQues.setOnClickListener(this)
    }

    fun addCardWithText(text: String) {
        val radioButton = MaterialRadioButton(requireContext())
        radioButton.text = text
        radioButton.setPadding(50, 0, 0, 0)
        radioGroup.addView(radioButton)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnIncreaseOptions -> {
                if (txtOptionsCount.text.toString() != defaultOptionsCount.toString()) {
                    txtOptionsCount.text = defaultOptionsCount.toString()
                    btnIncreaseOptions.isEnabled = false
                    btnDecreaseOptions.isEnabled = true
                    if (radioGroup.childCount < txtOptionsCount.text.toString().toInt()) {
                        btnAddOptions.isEnabled = true
                    }
                    for (i in 2 until radioGroup.childCount)
                        radioGroup.getChildAt(i).visibility = View.VISIBLE
                }
            }
            R.id.btnDecreaseOptions -> {
                if (txtOptionsCount.text.toString() == defaultOptionsCount.toString()) {
                    txtOptionsCount.text = (defaultOptionsCount - 2).toString()
                    btnIncreaseOptions.isEnabled = true
                    btnDecreaseOptions.isEnabled = false
                    if (radioGroup.childCount >= txtOptionsCount.text.toString().toInt()) {
                        btnAddOptions.isEnabled = false
                    }
                    for (i in radioGroup.childCount - 1 downTo 2)
                        radioGroup.getChildAt(i).visibility = View.GONE
                }
            }
            R.id.btnAddOptions -> {
                if (txtOptions.editText?.text.toString().trim().isEmpty()) {
                    txtOptions.error = "Please enter an option!"
                } else {
                    if (radioGroup.childCount < txtOptionsCount.text.toString().toInt()) {
                        txtOptions.error = null
                        val text = txtOptions.editText?.text
                        addCardWithText(text.toString())
                        txtOptions.editText?.setText("")
                        if (radioGroup.childCount == txtOptionsCount.text.toString().toInt()) {
                            btnAddOptions.isEnabled = false
                        }
                    }
                }
            }
            R.id.btnNextQues -> {
                val validated = validateFields()
                if (validated) {
                    addInfoToList()
                    resetFields()
                }
            }
            R.id.btnPrevQues -> {
                Firebase.auth.signOut()
            }
        }
    }

    fun validateFields(): Boolean {
        if (txtQuestionBody.editText?.text.toString().trim().isEmpty()) {
            txtQuestionBody.error = "Pls Enter the Question!"
            return false
        } else {
            txtQuestionBody.error = null
        }
        if (radioGroup.childCount < txtOptionsCount.text.toString().toInt()) {
            Snackbar.make(
                requireView(),
                "Enter at least ${txtOptionsCount.text} options!",
                Snackbar.LENGTH_SHORT
            ).show()
            return false
        } else {
            txtOptions.error = null
        }
        var anyChecked = false
        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as MaterialRadioButton
            if (radioButton.isChecked) {
                anyChecked = true
            }
        }
        if (!anyChecked) Snackbar.make(
            requireView(),
            "Select a correct option!",
            Snackbar.LENGTH_SHORT
        ).show()
        return anyChecked
    }

    fun resetFields() {
        btnAddOptions.isEnabled = true
        txtQuestionBody.editText?.setText("")
        txtOptions.editText?.setText("")
        txtQuestionBody.requestFocus()
        radioGroup.removeViews(0, txtOptionsCount.text.toString().toInt())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_tick, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val validated = validateFields()
        if (validated) {
            addInfoToList()
            postTest(questionList)
            shareDialog()

        }
        return super.onOptionsItemSelected(item)
    }

    fun addInfoToList() {
        val body = txtQuestionBody.editText?.text.toString()
        val questions = hashMapOf<String, Boolean>()
        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as MaterialRadioButton
            questions[radioButton.text.toString()] = radioButton.isChecked
        }
        val question =
            QuestionModel(body, questions)
        questionList.add(question)
    }

    fun postTest(questions: MutableList<QuestionModel>) {
        val database = Firebase.database.reference
        val user = Firebase.auth.currentUser
        val uidObj = database.child("tests").push()
        testID = uidObj.key.toString()
        val test = TestModel(
            testID,
            title,
            user?.uid.toString(),
            questions
        )

        uidObj.setValue(test)
    }

    fun shareDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val layout = layoutInflater.inflate(R.layout.share_dialog, null)
        val txtTitle = layout.findViewById<MaterialTextView>(R.id.txtTitleShare)
        val txtQuesCount = layout.findViewById<MaterialTextView>(R.id.txtQuesCountShare)
        val txtTestId = layout.findViewById<MaterialTextView>(R.id.txtShareTestId)
        val btnCopyId = layout.findViewById<Button>(R.id.btnCopyId)
        txtTitle.text = title
        txtQuesCount.text = "${questionList.size} Questions"
        txtTestId.text = testID
        btnCopyId.setOnClickListener {
            val clipboard = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("data", testID)
            clipboard.setPrimaryClip(clip)
        }
        builder.setView(layout)
        builder.setPositiveButton("Done") { dialog, which ->
            val navController = Navigation.findNavController(viewView)
            navController.popBackStack()
        }
        builder.setCancelable(false)
        builder.show()

    }
}

