package com.arshramgarhia.otest.dataClasses

data class TestModel(val Uid: String, val title: String, val created_by: String, val questions: MutableList<QuestionModel>){
    var score: String = ""
    constructor(Uid: String, title: String, created_by: String, questions: MutableList<QuestionModel>, score:String):this(Uid, title, created_by, questions){
        this.score = score
    }
}