package com.example.quizdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE "+
                QuizContract.CategoriesTable.TABLE_NAME + "( "+
                QuizContract.CategoriesTable._ID +"INTEGER PRIMARY KEY AUTOINCREMENT, "+
                QuizContract.CategoriesTable.COLUMN_NAME +" TEXT " + ")";


        final String SQL_CREATE_QUESTION_TABLE = "CREATE TABLE " +
                QuizContract.QuestionTable.TABLE_NAME + "( " +
                QuizContract.QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContract.QuestionTable.COLUMN_QUESTION+" TEXT, "+
                QuizContract.QuestionTable.COLUMN_OPTION1 +" TEXT, "+
                QuizContract.QuestionTable.COLUMN_OPTION2 +" TEXT, "+
                QuizContract.QuestionTable.COLUMN_OPTION3 + " TEXT, "+
                QuizContract.QuestionTable.COLUMN_ANSWER+ " INTEGER, " +
                QuizContract.QuestionTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuizContract.QuestionTable.COLUMN_CATEGORY_ID + " INTEGER, "+
                "FOREIGN KEY(" + QuizContract.QuestionTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                QuizContract.CategoriesTable.TABLE_NAME +"(" + QuizContract.CategoriesTable._ID +")" +"ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTION_TABLE);

        fillCategoriesTable();
        
        fillQuestionTable();


    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionTable.TABLE_NAME);
        onCreate(db);

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("programming");
        addCategory(c1);
        Category c2 = new Category("math");
        addCategory(c2);
        Category c3 = new Category("english");
        addCategory(c3);
    }
    private void addCategory(Category category){
        ContentValues cv = new ContentValues();
        cv.put(QuizContract.CategoriesTable.COLUMN_NAME,category.getName());
        db.insert(QuizContract.CategoriesTable.TABLE_NAME,null,cv);
    }

    private void fillQuestionTable() {
        Question q1 = new Question("A is correct","A","B","C",1,Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        addQquestion(q1);
        Question q2 = new Question("A is correct","A","B","C",1,Question.DIFFICULTY_MEDIUM, Category.MATH);
        addQquestion(q2);
        Question q3 = new Question("B is correct","A","B","C",2,Question.DIFFICULTY_MEDIUM, Category.MATH);
        addQquestion(q3);
        Question q4 = new Question("A is correct","A","B","C",1,Question.DIFFICULTY_HARD, Category.ENGLISH);
        addQquestion(q4);
        Question q5 = new Question("C is correct","A","B","C",3,Question.DIFFICULTY_HARD, Category.ENGLISH);
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
        cv.put(QuizContract.QuestionTable.COLUMN_CATEGORY_ID,question.getCategoryID());

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
                question.setId(c.getInt(c.getColumnIndex(QuizContract.QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNo(c.getInt(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_ANSWER)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_CATEGORY_ID)));

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
                question.setId(c.getInt(c.getColumnIndex(QuizContract.QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNo(c.getInt(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_ANSWER)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuizContract.QuestionTable.COLUMN_CATEGORY_ID)));

                questionList.add(question);

            }while (c.moveToNext());
        }
        c.close();
        return  questionList;

    }


}
