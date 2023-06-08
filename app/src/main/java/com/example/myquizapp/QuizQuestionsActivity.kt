package com.example.myquizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentposition:Int=1
    private var mQuestionList:ArrayList<Question>? = null
    private var mSelectOptionPosition:Int = 0


    private var progressBar:ProgressBar?=null;
    private var tvProgress:TextView?=null;
    private var tvQuestion:TextView?=null;
    private var ivImage: ImageView?=null;

    private var tvOptionOne:TextView?=null
    private var tvOptionTwo:TextView?=null
    private var tvOptionThree:TextView?=null
    private var tvOptionFour:TextView?=null

    private var mSubmitButton:Button?=null

    private var mUserName:String?=null

    private var correctAnswers:Int?=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)


        mUserName=intent.getStringExtra(Constants.USER_NAME)



        // initialize properties
        mQuestionList = Constants.getOuestion()
        setFunction()
    }

    private fun setFunction() {
        Log.i("QuestionList Size is ", "${mQuestionList?.size}")
        init()
        setQuestion()
        tvOptionOne?.setOnClickListener(this)
        tvOptionTwo?.setOnClickListener(this)
        tvOptionThree?.setOnClickListener(this)
        tvOptionFour?.setOnClickListener(this)



    }

    private fun setQuestion() {

        defaultOptionsView()

        var question: Question? = mQuestionList!![mCurrentposition - 1];
        Log.i("Question is ", "${question}")
        progressBar?.progress = mCurrentposition - 1
        tvProgress?.text = "${mCurrentposition - 1} / ${progressBar?.max}"
        tvQuestion?.text = question?.question
        tvOptionOne?.text = question?.optionOne
        tvOptionTwo?.text = question?.optionTwo
        tvOptionThree?.text = question?.optionThree
        tvOptionFour?.text = question?.optionFour
        question?.image?.let { ivImage?.setImageResource(it)
        }

        if(mCurrentposition == mQuestionList!!.size){
            mSubmitButton?.text="FINISH"
        }else{
            mSubmitButton?.text="SUBMIT"
        }
    }

    private fun init() {
        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tv_progress)
        tvQuestion = findViewById(R.id.tv_question)
        ivImage = findViewById(R.id.iv_image)
        tvOptionOne = findViewById(R.id.tv_option_one)
        tvOptionTwo = findViewById(R.id.tv_option_two)
        tvOptionThree = findViewById(R.id.tv_option_three)
        tvOptionFour = findViewById(R.id.tv_option_four)
        mSubmitButton = findViewById(R.id.btn_submit_quiz)
        mSubmitButton?.setOnClickListener {
            pressSubmit()
        }

    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        tvOptionOne?.let {
            options.add(0,it)
        }
        tvOptionTwo?.let {
            options.add(1,it)
        }
        tvOptionThree?.let {
            options.add(2,it)
        }
        tvOptionFour?.let {
            options.add(3,it)
        }

        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface= Typeface.DEFAULT
            option.background=ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }

    }

    private fun selectedOptionView(tv:TextView,selectedOptionNum:Int){
        defaultOptionsView()
        mSelectOptionPosition=selectedOptionNum

        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface,Typeface.BOLD)
        tv.background=ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.tv_option_one -> {
                tvOptionOne?.let {
                    selectedOptionView(it, 1)
                    //The it refers to the non-null value of tvOptionOne, and it will be passed as the
                    // first argument to the selectedOptionView function, with 1 being passed as the second argument.
                }
            }
            R.id.tv_option_two -> {
                tvOptionTwo?.let {
                    selectedOptionView(it, 2)
                    //The it refers to the non-null value of tvOptionOne, and it will be passed as the
                    // first argument to the selectedOptionView function, with 1 being passed as the second argument.
                }
            }
            R.id.tv_option_three -> {
                tvOptionThree?.let {
                    selectedOptionView(it, 3)
                    //The it refers to the non-null value of tvOptionOne, and it will be passed as the
                    // first argument to the selectedOptionView function, with 1 being passed as the second argument.
                }
            }
            R.id.tv_option_four -> {
                tvOptionFour?.let {
                    selectedOptionView(it, 4)
                    //The it refers to the non-null value of tvOptionOne, and it will be passed as the
                    // first argument to the selectedOptionView function, with 1 being passed as the second argument.
                }
            }
         }

    }

    private fun pressSubmit() {

        //Toast.makeText(this, "press", Toast.LENGTH_SHORT).show()
        if (mSelectOptionPosition == 0) {
            mCurrentposition++
            when {
                mCurrentposition <= mQuestionList!!.size -> {
                    setQuestion()
                }
                else->{
                    val intent: Intent = Intent(this,ResultActivity::class.java)
                    intent.putExtra(Constants.TOTAL_QUESTIONS,mQuestionList!!.size)
                    intent.putExtra(Constants.CORRECT_ANSWERS,correctAnswers)
                    intent.putExtra(Constants.USER_NAME,mUserName)
                    startActivity(intent)
                    finish()
                }
            }
        } else {
            val question = mQuestionList?.get(mCurrentposition - 1)

            if (question!!.correctAnswer != mSelectOptionPosition) {
                answerView(mSelectOptionPosition, R.drawable.wrong_option_border_bg)
                answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                mCurrentposition++
                setQuestion()
            } else {
                correctAnswers= correctAnswers?.plus(1)
                answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                if (mCurrentposition == mQuestionList!!.size) {
                    mSubmitButton?.text = "Finish"
                } else {
                    mSubmitButton?.text = "Go To next Question"
                }
                mSelectOptionPosition = 0
            }
        }
    }

    private fun answerView(answer:Int,drawbleView:Int){
        when(answer){
            1->{
                tvOptionOne?.background= ContextCompat.getDrawable(
                    this,
                    drawbleView
                )
            }
            2->{
                tvOptionTwo?.background= ContextCompat.getDrawable(
                    this,
                    drawbleView
                )
            }
            3->{
                tvOptionThree?.background= ContextCompat.getDrawable(
                    this,
                    drawbleView
                )
            }
            4->{
                tvOptionFour?.background= ContextCompat.getDrawable(
                    this,
                    drawbleView
                )
            }
        }
    }

}