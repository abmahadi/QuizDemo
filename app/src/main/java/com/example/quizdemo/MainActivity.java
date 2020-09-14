package com.example.quizdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ =1;
    public static final String SHARED_PREFS ="sharedPrefs";
    public static  final String KEY_HIGHSCORE ="keyHighscore";

    private Button startQuiz;
    private TextView tvHighScore;
    private int highscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startQuiz = findViewById(R.id.button_start_quiz);
        tvHighScore = findViewById(R.id.textview_highscore);

        loadHighscore();

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,QuizActivity.class);
                startActivityForResult(intent,REQUEST_CODE_QUIZ);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_QUIZ){
            if(resultCode == RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE,0);
                if(score> highscore){
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadHighscore(){
        SharedPreferences prefs =getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        highscore =prefs.getInt(KEY_HIGHSCORE,0);
        tvHighScore.setText("Highscore : "+highscore);
    }

    private void updateHighscore(int highscoreNew) {
        highscore =highscoreNew;
        tvHighScore.setText("Highscore : "+highscore);

        SharedPreferences prefs = getSharedPreferences( SHARED_PREFS , MODE_PRIVATE) ;
        SharedPreferences.Editor editor =prefs.edit();

        editor.putInt(KEY_HIGHSCORE,highscore);
        editor.apply();



    }
}
