package com.example.quizdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizBdHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quizDemo.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;
    public QuizBdHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTION_TABLE = "CREATE TABLE " +
                QuizContract.QuestionTable.TABLE_NAME + "(" +
                QuizContract.QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContract.QuestionTable.COLUMN_QUESTION+" TEXT, "+
                QuizContract.QuestionTable.COLUMN_OPTION1 +" TEXT, "+
                QuizContract.QuestionTable.COLUMN_OPTION2 +" TEXT, "+
                QuizContract.QuestionTable.COLUMN_OPTION3 + " TEXT, "+
                QuizContract.QuestionTable.COLUMN_ANSWER+ " INTEGER, " +
                QuizContract.QuestionTable.COLUMN_DIFFICULTY + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_QUESTION_TABLE);
        
        fillQuestionTable();


    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionTable.TABLE_NAME);
        onCreate(db);

    }

    private void fillQuestionTable() {
        Question q1 = new Question("A is correct","A","B","C",1,Question.DIFFICULTY_EASY);
        addQquestion(q1);
        Question q2 = new Question("A is correct","A","B","C",1,Question.DIFFICULTY_MEDIUM);
        addQquestion(q2);
        Question q3 = new Question("A is correct","A","B","C",1,Question.DIFFICULTY_MEDIUM);
        addQquestion(q3);
        Question q4 = new Question("A is correct","A","B","C",1,Question.DIFFICULTY_HARD);
        addQquestion(q4);
        Question q5 = new Question("A is correct","A","B","C",1,Question.DIFFICULTY_HARD);
        addQquestion(q5);

    }
    private void addQquestion(Question question){
        ContentValues cv= new ContentValues();
        cv.put(QuizContract.QuestionTable.COLUMN_QUESTION,question.getQuestion());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION1,question.getOption1());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION2,question.getOption2());
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION3,question.getOption3());
        cv.put(QuizContract.QuestionTable.COLUMN_ANSWER,question.getAnswerNo());
        cv.put(QuizContract.QuestionTable.COLUMN_DIFFICULTY,question.getDifficulty());

        db.insert(QuizContract.QuestionTable.TABLE_NAME,null,cv);
    }

    public ArrayList<Question> getAllQuestion(){

        ArrayList<Question> questionList =new ArrayList<>();

        db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+ QuizContract.QuestionTable.TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                Question question =new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNo(c.getInt(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_ANSWER)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_DIFFICULTY)));

                questionList.add(question);

            }while (c.moveToNext());
        }
        c.close();
        return  questionList;

    }

    public ArrayList<Question> getQuestion(String difficulty){

        ArrayList<Question> questionList =new ArrayList<>();

        db = getReadableDatabase();
        String[] selectionArgs = new String[]{difficulty};

        Cursor c = db.rawQuery("SELECT * FROM "+ QuizContract.QuestionTable.TABLE_NAME +
                " WHERE " + QuizContract.QuestionTable.COLUMN_DIFFICULTY + " = ?",selectionArgs);
        if(c.moveToFirst())
        {
            do
            {
                Question question =new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNo(c.getInt(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_ANSWER)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_DIFFICULTY)));

                questionList.add(question);

            }while (c.moveToNext());
        }
        c.close();
        return  questionList;

    }


}
