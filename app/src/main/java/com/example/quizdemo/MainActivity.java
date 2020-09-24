package com.example.quizdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ =1;
    public static final String EXTRA_CATEGORY_ID= "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";

    public static final String SHARED_PREFS ="sharedPrefs";
    public static  final String KEY_HIGHSCORE ="keyHighscore";

    private Button startQuiz;
    private TextView tvHighScore;
    private Spinner difficultySpinner;
    private Spinner categorySpinner;

    private int highscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startQuiz = findViewById(R.id.button_start_quiz);
        tvHighScore = findViewById(R.id.textview_highscore);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        categorySpinner = findViewById(R.id.categorySpinner);

        QuizBdHelper quizBdHelper = QuizBdHelper.getInstance(this);
        List<Category> categories = quizBdHelper.getAllCategory();
        ArrayAdapter<Category> adapterCategory = new ArrayAdapter<>(this , android.R.layout.simple_spinner_item,categories);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapterCategory);

        String[] difficultyLevels = Question.getAllDifficultyLevels();
        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapterDifficulty);

        loadHighscore();

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category selectedCategory = (Category) categorySpinner.getSelectedItem();
                int categoryID = selectedCategory.getId();
                String categoryName = selectedCategory.getName();
                String difficulty = difficultySpinner.getSelectedItem().toString();

                Intent intent =new Intent(MainActivity.this,QuizActivity.class);
                intent.putExtra(EXTRA_CATEGORY_ID,categoryID);
                intent.putExtra(EXTRA_CATEGORY_NAME,categoryName);
                intent.putExtra(EXTRA_DIFFICULTY,difficulty);
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
