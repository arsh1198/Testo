package com.arsh.testo.dataClasses

data class TestModel(val Uid: String, val title: String, val created_by: String, val questions: MutableList<QuestionModel>)