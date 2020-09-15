package com.example.quizdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static  final String EXTRA_SCORE = "extraScore";
    private static  final long COUNTDOWN_IN_MILLIS = 30000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT ="keyMillisLeft";
    private static final String KEY_ANSWERED ="keyAnswered";
    private static final String KEY_QUESTION_LIST = "ketQuestionList";

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
    private ColorStateList textColorDefoltCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> questionList;

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

        if(savedInstanceState == null){

        QuizBdHelper bdHelper= new QuizBdHelper(QuizActivity.this);
       // questionList = bdHelper.getAllQuestion();
        questionList =bdHelper.getQuestion("Medium");
        questionCountertotal =questionList.size();
        Collections.shuffle(questionList);
        showNextQuestion();
        }else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountertotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            scorePoint = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answared = savedInstanceState.getBoolean(KEY_ANSWERED);

            if(!answared){
                startCountDown();
            }else {
                updateCountDownText();
                showSolution();
            }
        }

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
        countDownTimer.cancel();

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

            timeLeftInMillis =COUNTDOWN_IN_MILLIS;
            startCountDown();


        }else{
            finishQuiz();
        }

    }

    private void startCountDown() {

        countDownTimer =new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis =l;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                timeLeftInMillis=0;
                updateCountDownText();
                checkAnswer();

            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int second  = (int)(timeLeftInMillis /1000) % 60;

        String timeFormet = String.format(Locale.getDefault(),"%02d:%02d",minutes,second);
        tvTimeCounter.setText(timeFormet);

        if(timeLeftInMillis < 10000){
            tvTimeCounter.setTextColor(Color.RED);
        }else {
            tvTimeCounter.setTextColor(textColorDefoltCd);
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
        textColorDefoltCd =tvTimeCounter.getTextColors();



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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE,scorePoint);
        outState.putInt(KEY_QUESTION_COUNT,questionCounter);
        outState.putBoolean(KEY_ANSWERED,answared);
        outState.putParcelableArrayList(KEY_QUESTION_LIST,questionList);
    }
}
