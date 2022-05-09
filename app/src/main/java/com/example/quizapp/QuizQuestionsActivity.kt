package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var currentPosition: Int = 1
    private var questionsList: ArrayList<Question>? = null
    private var mSelectedOption: Int = 0
    private var mUserName: String? = null
    private var mCorrectAnswers: Int = 0

    private var progressBar: ProgressBar? = null
    private var tvProgress: TextView? = null
    private var textQuestion: TextView? = null
    private var tvImage: ImageView? = null

    private var optionOne: TextView? = null
    private var optionTwo: TextView? = null
    private var optionThree: TextView? = null
    private var optionFour: TextView? = null

    private var btnSubmit: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        progressBar = findViewById(R.id.progressBar)
        tvProgress = findViewById(R.id.tvProgress)
        textQuestion = findViewById(R.id.textQuestion)
        tvImage = findViewById(R.id.tvImage)

        optionOne = findViewById(R.id.tvoptionOne)
        optionTwo = findViewById(R.id.tvoptionTwo)
        optionThree = findViewById(R.id.tvoptionThree)
        optionFour = findViewById(R.id.tvoptionFour)
        btnSubmit = findViewById(R.id.btn_Submit)

        optionOne?.setOnClickListener(this)
        optionTwo?.setOnClickListener(this)
        optionThree?.setOnClickListener(this)
        optionFour?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)


        questionsList = Constants.getQuestions()

        setQuestion()
    }

    private fun setQuestion() {
        defaultOptionsView()
        val question: Question = questionsList!![currentPosition - 1]
        tvImage?.setImageResource(question.image)
        progressBar?.progress = currentPosition!!
        tvProgress?.text = "$currentPosition/${progressBar?.max}"
        textQuestion?.text = question.question

        optionOne?.text = question.optionOne
        optionTwo?.text = question.optionTwo
        optionThree?.text = question.optionThree
        optionFour?.text = question.optionFour

        if (currentPosition == questionsList!!.size) {
            btnSubmit?.text = "FINISH"
        } else {
            btnSubmit?.text = "NEXT"
        }
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        optionOne?.let {
            options.add(0, it)
        }
        optionTwo?.let {
            options.add(1, it)
        }
        optionThree?.let {
            options.add(2, it)
        }
        optionFour?.let {
            options.add(3, it)
        }

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOption: Int) {
        defaultOptionsView()

        mSelectedOption = selectedOption
        tv.setTextColor((Color.parseColor("#363A43")))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvoptionOne -> {
                optionOne?.let {
                    selectedOptionView(it, 1)
                }
            }

            R.id.tvoptionTwo -> {
                optionTwo?.let {
                    selectedOptionView(it, 2)
                }
            }

            R.id.tvoptionThree -> {
                optionThree?.let {
                    selectedOptionView(it, 3)
                }
            }

            R.id.tvoptionFour -> {
                optionFour?.let {
                    selectedOptionView(it, 4)
                }
            }

        R.id.btn_Submit -> {
            if(mSelectedOption == 0) {
                currentPosition++

                when(currentPosition <= questionsList!!.size){
                    true ->setQuestion()
                    else -> {
                        val intent = Intent(this, Result::class.java)
                        intent.putExtra(Constants.USER_NAME, mUserName)
                        intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                        intent.putExtra(Constants.TOTAL_QUESTIONS, questionsList?.size)
                        startActivity(intent)
                        finish()
                    }
                }
            }else {
                val question = questionsList?.get(currentPosition-1)
                if(question!!.correctAnswer != mSelectedOption){
                    answerView(mSelectedOption, R.drawable.wrong_option_border_bg)
                } else {
                    mCorrectAnswers++
                }
                answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                if(currentPosition == questionsList!!.size){
                    btnSubmit?.text = "FINISH"
                }else{
                    btnSubmit?.text = "GO TO NEXT QUESTION"
                }

                mSelectedOption = 0
            }
        }
    }




    }
    private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1-> {
                optionOne?.background = ContextCompat.getDrawable(this, drawableView)
            }
            2-> {
                optionTwo?.background = ContextCompat.getDrawable(this, drawableView)
            }
            3-> {
                optionThree?.background = ContextCompat.getDrawable(this, drawableView)
            }
            4-> {
                optionFour?.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }
}