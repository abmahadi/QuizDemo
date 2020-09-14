package com.example.quizdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    public static  final String EXTRA_SCORE = "extraScore";

    private TextView tvScore;
    private TextView tvQuestionCount;
    private TextView tvTimeCounter;
    private TextView tvQuestion;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private Button btnConfirm;

    private ColorStateList textColorDefoltRb;

    private List<Question> questionList;

    private  int questionCounter;
    private  int questionCountertotal;
    private Question currentQuestion;

    private  boolean answared;
    private  int scorePoint;
    private long backPressedTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        init();

        showNextQuestion();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answared){
                    if(radioButton1.isChecked() || radioButton2.isChecked() || radioButton3.isChecked()){
                        checkAnswer();
                    }else {
                        Toast.makeText(QuizActivity.this, "Please select an Answer", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    showNextQuestion();
                }
            }
        });
    }

    private void checkAnswer() {

        answared = true;
        RadioButton rbSelected =findViewById(radioGroup.getCheckedRadioButtonId());

        int answerNr = radioGroup.indexOfChild(rbSelected)+1;

        if(answerNr == currentQuestion.getAnswerNo()){
            scorePoint++;
            tvScore.setText("Score :"+scorePoint);
        }
        showSolution();
    }

    private void showSolution() {
        radioButton1.setTextColor(Color.RED);
        radioButton2.setTextColor(Color.RED);
        radioButton3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNo())
        {
            case 1:
                radioButton1.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 1 is Correct");
                break;
            case 2:
                radioButton2.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 2 is Correct");
                break;
            case 3:
                radioButton3.setTextColor(Color.GREEN);
                tvQuestion.setText("Answer 3 is Correct");
                break;
        }
        if(questionCounter<questionCountertotal)
        {
            btnConfirm.setText("Next");
        }else {
            btnConfirm.setText("Finish");
        }
    }

    private void showNextQuestion() {

        radioButton1.setTextColor(textColorDefoltRb);
        radioButton2.setTextColor(textColorDefoltRb);
        radioButton3.setTextColor(textColorDefoltRb);

        radioGroup.clearCheck();

        if(questionCounter < questionCountertotal){
            currentQuestion = questionList.get(questionCounter);

            tvQuestion.setText(currentQuestion.getQuestion());
            radioButton1.setText(currentQuestion.getOption1());
            radioButton2.setText(currentQuestion.getOption2());
            radioButton3.setText(currentQuestion.getOption3());

            questionCounter++;

            tvQuestionCount.setText("Question :"+questionCounter+"/"+questionCountertotal);
            answared =false;
            btnConfirm.setText("Confirm");


        }else{
            finishQuiz();
        }

    }

    private void finishQuiz() {
        Intent resultIntent =new Intent();
        resultIntent.putExtra(EXTRA_SCORE,scorePoint);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    private void init() {
        tvScore =findViewById(R.id.textview_score);
        tvQuestionCount =findViewById(R.id.textview_questionCount);
        tvTimeCounter =findViewById(R.id.textview_timeCounter);
        tvQuestion = findViewById(R.id.textview_Question);
        radioGroup = findViewById(R.id.radiogroup_answareOption);
        radioButton1 = findViewById(R.id.radiobutton_option1);
        radioButton2 = findViewById(R.id.radiobutton_option2);
        radioButton3 = findViewById(R.id.radiobutton_option3);
        btnConfirm = findViewById(R.id.button_confirm);

        textColorDefoltRb =radioButton1.getTextColors();

        QuizBdHelper bdHelper= new QuizBdHelper(this);

        questionList = bdHelper.getAllQuestion() ;

        questionCountertotal =questionList.size();
        Collections.shuffle(questionList);

    }

    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        }else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
         backPressedTime = System.currentTimeMillis();
    }
}
