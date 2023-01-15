package com.example.myapplication;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class MyClickableSpan extends ClickableSpan {




    @Override
    public void onClick(@NonNull View view) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.BLACK);
        ds.setUnderlineText(false);
    }
}