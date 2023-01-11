package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

public class QCM extends AppCompatActivity {

    int i=0;
    int randomReal=0;
    int randomFalse1=0;
    int randomFalse2=0;
    int randomFalse3=0;
    int realPosition=0;
    String[] questions = new String[] { "Quel est la capital de la France ?", "A quelle température boue l'eau ?", "La tomate est un fruit ou un legume ?" };

    String[] reponses = new String[] { "Paris", "100°", "Fruit", "Fraise", "Pizza", "Manger", "Oui", "Non", "Nul", "Test"};



    public void displayMsg(int button){
        if (realPosition==button-1){
            Toast.makeText(this, "Bouton cliqué : "+String.valueOf(button)+ " CORRECT", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(this, "Bouton cliqué : "+String.valueOf(button)+ " INCORRECT", Toast.LENGTH_SHORT).show();

        }
        initiateNew();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_c_m);

        initiateNew();



    }

    public void initiateNew(){
        Button ans1 = (Button) findViewById(R.id.answer1);
        Button ans2 = (Button) findViewById(R.id.answer2);
        Button ans3 = (Button) findViewById(R.id.answer3);
        Button ans4 = (Button) findViewById(R.id.answer4);
        TextView questionText = (TextView) findViewById(R.id.questionDisplay);

        randomReal = (int) Math.floor(Math.random() * (questions.length));
        randomFalse1 = (int) Math.floor(Math.random() * (reponses.length));
        randomFalse2 = (int) Math.floor(Math.random() * (reponses.length));
        randomFalse3 = (int) Math.floor(Math.random() * (reponses.length));

        while (randomReal == randomFalse1 || randomReal == randomFalse2 || randomReal == randomFalse3 || randomFalse1 == randomFalse2 || randomFalse1 == randomFalse3 || randomFalse2 == randomFalse3) {
            randomFalse1 = (int) Math.floor(Math.random() * (reponses.length));
            randomFalse2 = (int) Math.floor(Math.random() * (reponses.length));
            randomFalse3 = (int) Math.floor(Math.random() * (reponses.length));
        }

        questionText.setText(questions[randomReal]);

        realPosition = (int) Math.floor(Math.random() * (4));

        if (realPosition == 0) {
            ans1.setText(reponses[randomReal]);
            ans2.setText(reponses[randomFalse1]);
            ans3.setText(reponses[randomFalse2]);
            ans4.setText(reponses[randomFalse3]);
        } else if (realPosition == 1) {
            ans1.setText(reponses[randomFalse1]);
            ans2.setText(reponses[randomReal]);
            ans3.setText(reponses[randomFalse2]);
            ans4.setText(reponses[randomFalse3]);
        } else if (realPosition == 2) {
            ans1.setText(reponses[randomFalse2]);
            ans2.setText(reponses[randomFalse1]);
            ans3.setText(reponses[randomReal]);
            ans4.setText(reponses[randomFalse3]);

        } else if (realPosition == 3) {
            ans1.setText(reponses[randomFalse3]);
            ans2.setText(reponses[randomFalse1]);
            ans3.setText(reponses[randomFalse2]);
            ans4.setText(reponses[randomReal]);

        }
    }

    public void cliqueBt1(View v){
        displayMsg(1);
    }
    public void cliqueBt2(View v){
        displayMsg(2);
    }
    public void cliqueBt3(View v){
        displayMsg(3);
    }
    public void cliqueBt4(View v){
        displayMsg(4);
    }


}