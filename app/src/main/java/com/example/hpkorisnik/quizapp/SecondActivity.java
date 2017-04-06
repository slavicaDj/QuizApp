package com.example.hpkorisnik.quizapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SecondActivity extends AppCompatActivity {

    String nickname;

    String correctAnswer;
    int currentQuestionIndex = 0;
    int numberOfQuestions = 10;
    ArrayList<Question> questions = new ArrayList<>();

    Player player;

    TextView textViewQuestionNo;
/*    TextView textViewAnswer1;
    TextView textViewAnswer2;
    TextView textViewAnswer3;
    TextView textViewAnswer4;*/

    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //get nickname from MainActivity
        nickname = getIntent().getExtras().getString(getString(R.string.nickname));
        player = new Player(nickname,0);

        //fetch widgets
        textViewQuestionNo = (TextView)findViewById(R.id.textViewQuestionNo);
/*        textViewAnswer1 = (TextView)findViewById(R.id.textViewAnswer1);
        textViewAnswer2 = (TextView)findViewById(R.id.textViewAnswer2);
        textViewAnswer3 = (TextView)findViewById(R.id.textViewAnswer3);
        textViewAnswer4 = (TextView)findViewById(R.id.textViewAnswer4);*/
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton)findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton)findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton)findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton)findViewById(R.id.radioButton4);

        getQuestions();

    }


    public void onClick(View view) {
        //no radio button is checked, so show Toast
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "You must select answer", Toast.LENGTH_SHORT).show();
        }
        //some radio button is checked
        else {
            // adding points
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radioButton1:
                    if (radioButton1.getText().toString().equals(correctAnswer)) player.incrementPoints();
                    radioButton1.setSelected(false);
                    break;
                case R.id.radioButton2:
                    if (radioButton2.getText().toString().equals(correctAnswer)) player.incrementPoints();
                    radioButton2.setSelected(false);
                    break;
                case R.id.radioButton3:
                    if (radioButton3.getText().toString().equals(correctAnswer)) player.incrementPoints();
                    radioButton3.setSelected(false);
                    break;
                case R.id.radioButton4:
                    if (radioButton4.getText().toString().equals(correctAnswer)) player.incrementPoints();
                    radioButton4.setSelected(false);
                    break;
                /*case R.id.radioButton1:
                    if (textViewAnswer1.getText().toString().equals(correctAnswer)) player.incrementPoints();
                    radioButton1.setSelected(false);
                    break;
                case R.id.radioButton2:
                    if (textViewAnswer2.getText().toString().equals(correctAnswer)) player.incrementPoints();
                    radioButton2.setSelected(false);
                    break;
                case R.id.radioButton3:
                    if (textViewAnswer3.getText().toString().equals(correctAnswer)) player.incrementPoints();
                    radioButton3.setSelected(false);
                    break;
                case R.id.radioButton4:
                    if (textViewAnswer4.getText().toString().equals(correctAnswer)) player.incrementPoints();
                    radioButton4.setSelected(false);
                    break;*/
            }
            //checking if player came to the last question
            if (currentQuestionIndex == (numberOfQuestions-1)) {
                System.out.println(player.getPoints());

                setScore();


            }
            else {
                currentQuestionIndex++;
                updateUI();
            }
            radioGroup.clearCheck();
        }
    }

    public void updateUI() {
        try {
            //question with wanted index, update UI
            textViewQuestionNo.setText((currentQuestionIndex+1) + ". " + questions.get(currentQuestionIndex).getQuestion());
/*            textViewAnswer1.setText(questions.get(currentQuestionIndex).getAnswers().get(0));
            textViewAnswer2.setText(questions.get(currentQuestionIndex).getAnswers().get(1));
            textViewAnswer3.setText(questions.get(currentQuestionIndex).getAnswers().get(2));
            textViewAnswer4.setText(questions.get(currentQuestionIndex).getAnswers().get(3));*/
            radioButton1.setText(questions.get(currentQuestionIndex).getAnswers().get(0));
            radioButton2.setText(questions.get(currentQuestionIndex).getAnswers().get(1));
            radioButton3.setText(questions.get(currentQuestionIndex).getAnswers().get(2));
            radioButton4.setText(questions.get(currentQuestionIndex).getAnswers().get(3));
            int indexOfCorrectAnswer = Integer.valueOf(questions.get(currentQuestionIndex).getCorrectAnswer());
            correctAnswer = questions.get(currentQuestionIndex).getAnswers().get(indexOfCorrectAnswer-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);

        builder.setTitle("Quit")
                .setView(view)
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SecondActivity.this.finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void getQuestions() {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(getString(R.string.api_endpoint_questions), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                        String question = response.getJSONArray("data").getJSONObject(i).getString("question");
                        ArrayList<String> answers = new ArrayList<>();
                        answers.add(response.getJSONArray("data").getJSONObject(i).getString("answer1"));
                        answers.add(response.getJSONArray("data").getJSONObject(i).getString("answer2"));
                        answers.add(response.getJSONArray("data").getJSONObject(i).getString("answer3"));
                        answers.add(response.getJSONArray("data").getJSONObject(i).getString("answer4"));
                        String correctAnswer = response.getJSONArray("data").getJSONObject(i).getString("correct_answer");
                        questions.add(new Question(question,correctAnswer,answers));
                    }
                    //update gui with first question
                    System.out.println(questions.get(0));
                    updateUI();
                }
                catch (Exception e) {
                    System.out.print(e);
                }
            }
        });
    }

    public void setScore() {

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("name", player.getNickname());
        params.put("score", player.getPoints());

        client.post(getString(R.string.api_endpoint_set_score), params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        //open MainActivity again
                        Intent intent = new Intent(SecondActivity.this,MainActivity.class);
                        startActivity(intent);

                        //close this activity
                        SecondActivity.this.finish();
                    }
                }
        );
    }

}
