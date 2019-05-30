package com.nghiatv.duoihinhbatchu.view;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nghiatv.duoihinhbatchu.R;
import com.nghiatv.duoihinhbatchu.data.Data;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtHeart, txtCoin, txtResult;
    private FrameLayout flImage;
    private LinearLayout llAnswer, llChoose1, llChoose2;
    private Button btnNext;
    private Button[] btnAnswer;
    private ImageView[] imgQuestions;

    private Random random;
    private String result;
    private int rd;
    private int coin;
    private int heart;
    private int i;
    private int count;
    private boolean complete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        initOther();

        showImageQuestion();
        showButtonAnswer();
        showButtonChoose();
    }

    private void initializeComponents() {
        txtHeart = (TextView) findViewById(R.id.txtHeart);
        txtCoin = (TextView) findViewById(R.id.txtCoin);
        txtResult = (TextView) findViewById(R.id.txtResult);

        btnNext = (Button) findViewById(R.id.btnNext);

        flImage = (FrameLayout) findViewById(R.id.flImage);
        llAnswer = (LinearLayout) findViewById(R.id.llAnswer);
        llChoose1 = (LinearLayout) findViewById(R.id.llChoose1);
        llChoose2 = (LinearLayout) findViewById(R.id.llChoose2);

        btnNext.setOnClickListener(this);
    }

    private void initOther() {
        imgQuestions = new ImageView[Data.QUESTIONS.length];

        random = new Random();
        rd = random();
        result = "";
        coin = 0;
        heart = 5;
        i = 0;
        count = 0;
        complete = false;
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;

        if (!complete) {
            btnAnswer[i].setText(btn.getText());
            result += btn.getText();
            i++;
            v.setVisibility(View.INVISIBLE);
            count++;
        }

        if (count == Data.ANSWERS[rd].length()) {
            complete = true;

            if (result.equals(Data.ANSWERS[rd])) {

                for (int i = 0; i < Data.ANSWERS[rd].length(); i++) {
                    btnAnswer[i].setBackgroundResource(R.drawable.tile_true);
                }
                txtResult.setText("Bạn đã trả lời đúng !!!");
                btnNext.setText("CAU TIEP THEO");
                btnNext.setVisibility(View.VISIBLE);

                switch (v.getId()) {
                    case R.id.btnNext:
                        coin += 100;
                        txtCoin.setText(coin + "");
                        clickNext();
                        break;

                    default:
                        break;
                }
                return;

            } else {

                for (int i = 0; i < Data.ANSWERS[rd].length(); i++) {
                    btnAnswer[i].setBackgroundResource(R.drawable.tile_false);
                }
                txtResult.setText("Bạn đã trả lời sai !!!");
                btnNext.setText("CAU TIEP THEO");
                btnNext.setVisibility(View.VISIBLE);

                switch (v.getId()) {
                    case R.id.btnNext:
                        heart -= 1;
                        txtHeart.setText(heart + "");
                        clickNext();
                        break;
                }
                return;
            }
        }

        if (heart == 0) {
            Toast.makeText(this, "Bạn đã thua", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void clickNext() {
        //btnNext.setLayoutParams(new LinearLayout.LayoutParams(450, 150));
        btnNext.setVisibility(View.GONE);
        flImage.removeAllViews();
        llAnswer.removeAllViews();
        llChoose1.removeAllViews();
        llChoose2.removeAllViews();
        rd = random();
        showImageQuestion();
        showButtonAnswer();
        showButtonChoose();
        txtResult.setText("");
        result = "";
        count = 0;
        i = 0;
        complete = false;
    }

    private void showImageQuestion() {
        imgQuestions[rd] = new ImageView(this);
        imgQuestions[rd].setImageResource(Data.QUESTIONS[rd]);
        flImage.addView(imgQuestions[rd]);
    }

    private void showButtonAnswer() {
        btnAnswer = new Button[Data.ANSWERS[rd].length()];

        for (int i = 0; i < Data.ANSWERS[rd].length(); i++) {
            Button btn = new Button(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams(120, 130));
            btn.setId(i);
            btn.setBackgroundResource(R.drawable.button_xam);
            llAnswer.addView(btn);
            btnAnswer[i] = (Button) findViewById(btn.getId());
        }
    }

    private void showButtonChoose() {
        ArrayList<Integer> arr = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            Button btn = new Button(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams(120, 130));
            btn.setBackgroundResource(R.drawable.tile_hover);
            btn.setOnClickListener(this);
            while (btn.getText().equals("")) {
                int tmp = random.nextInt(16);
                if (check(arr, tmp)) {
                    btn.setText((CharSequence) randomQuestions().get(tmp));
                    randomQuestions().remove(tmp);
                    arr.add(tmp);
                }
            }
            llChoose1.addView(btn);
        }
        for (int i = 0; i < 8; i++) {
            Button btn = new Button(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams(120, 130));
            btn.setBackgroundResource(R.drawable.tile_hover);
            btn.setOnClickListener(this);
            while (btn.getText() == "") {
                int tmp = random.nextInt(16);
                if (check(arr, tmp)) {
                    btn.setText((CharSequence) randomQuestions().get(tmp));
                    randomQuestions().remove(tmp);
                    arr.add(tmp);
                }
            }
            llChoose2.addView(btn);
        }
    }

    private ArrayList randomQuestions() {
        ArrayList<String> arr = new ArrayList<>();
        int tm = random.nextInt(25) + 65;
        for (int i = 0; i < Data.ANSWERS[rd].length(); i++) {
            arr.add(Data.ANSWERS[rd].charAt(i) + "");
        }
        for (int i = 0; i < 16 - Data.ANSWERS[rd].length(); i++) {
            arr.add((char) tm + "");
        }
        return arr;
    }

    private int random() {
        ArrayList<Integer> arrRandom = new ArrayList<>();
        int randomNumber = 0;
        while (check(arrRandom, randomNumber)) {
            randomNumber = random.nextInt(Data.QUESTIONS.length);
            arrRandom.add(randomNumber);
        }
        return randomNumber;
    }

    private boolean check(ArrayList<Integer> arrRandom, int randomNumber) {
        for (int i = 0; i < arrRandom.size(); i++) {
            if (randomNumber == arrRandom.get(i)) {
                return false;
            }
        }
        return true;
    }
}
