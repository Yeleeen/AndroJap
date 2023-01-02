package com.example.myapplication.Class;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Spanned;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RubySpan extends ReplacementSpan {

    private static final float FONTSIZE_SCALE = 0.5f;
    private String ruby;
    private int rubyLength;
    private float offsetX;
    private boolean useColor;
    private int color;
    private boolean underline;
    private float spaceLeft;
    private boolean enoughSpaceLeft;
    private float measureSize;
    private boolean consecutiveRuby;
    private float extraSkip;
    private static final float FACTORL = 0.5f;
    private static final float FACTORS = 0.2f;
    private Paint.FontMetrics fm;
    private float widthDiff;


    private float fontSize, textWidth, rubyWidth;

    private float newTextWidth, newRubyWidth;

    enum RelativeLength {
        UNKNOWN,
        NORMAL,
        TOO_LONG,
        TOO_SHORT
    }
    private RelativeLength relativeLength = RelativeLength.UNKNOWN;
    public RubySpan(String furigana) {
        this.ruby = furigana;
        this.useColor = true;
        this.color = Color.BLACK;
        this.underline = false;
    }
    public float getExceededWidth() {
        return Math.max(0, newRubyWidth - newTextWidth);
    }
    public float getSpaceLeft() {
        return spaceLeft;
    }
    private void measureRun(Paint paint, CharSequence text, int start, int end) {

        final int textLength = end - start;
        rubyLength = ruby.length();
        fontSize = paint.getTextSize();
        textWidth = fontSize * textLength;
        fm = paint.getFontMetrics();
        rubyWidth = fontSize * FONTSIZE_SCALE * rubyLength;

        /**
         * Layout strategy:
         * 1) Don't do anything if the text contains only one character.
         * 2) Align ruby and text if they have equal number of characters.
         * 3) Stretch the text if `rubyWith > textWidth + FACTORS * fontSize`.
         * 4) Stretch the ruby if the text has fewer characters and `rubyWith < textWidth`.
         */
        newTextWidth = textWidth;
        newRubyWidth = rubyWidth;
        if (textLength == rubyLength) {
            relativeLength = RelativeLength.TOO_SHORT;
            // both left and right have half textSize left
            widthDiff = textWidth - FACTORL * fontSize - rubyWidth;
            newRubyWidth = rubyWidth + widthDiff;
        } else if (textLength > 1 && rubyWidth > textWidth + FACTORS * fontSize) {
            relativeLength = RelativeLength.TOO_LONG;
            widthDiff = rubyWidth - (textWidth + FACTORS * fontSize);
            newTextWidth = textWidth + widthDiff;
        } else if (textLength > 1 && textLength < rubyLength && rubyWidth < textWidth) {
            relativeLength = RelativeLength.TOO_SHORT;
            widthDiff = textWidth - FACTORS * fontSize - rubyWidth;
            newRubyWidth = textWidth - FACTORS * fontSize;
        } else {
            relativeLength = RelativeLength.NORMAL;
        }

        offsetX = (newTextWidth - newRubyWidth) / 2;

        // FIXME If the ruby is too long, it will exceed the right border
        // and becomes partially invisible.
        measureSize = newTextWidth;

        Object[] objs = ((Spanned)text).getSpans(0, start, this.getClass());
        int len = objs.length;
        if (len > 0 && ((Spanned)text).getSpanEnd(objs[len - 1]) == start) {
            consecutiveRuby = true;
            final RubySpan prevRubySpan = (RubySpan)objs[len - 1];

            /* ------------------------------------------------------
             *
             *       exceeded width   abs(offsetX)
             *                   |       |
             *                   v       v
             *                 |  |   |     |
             *  ruby ruby ruby ruby   ruby ruby ruby ruby ruby ruby
             *    TEXT TEXT TEXT            TEXT TEXT TEXT
             *
             * ------------------------------------------------------
             */
            extraSkip = prevRubySpan.getExceededWidth();
            extraSkip += Math.max(0, -offsetX) + 5;

            // FIXME buggy
            float spaceLeft = prevRubySpan.getSpaceLeft();
            if (spaceLeft > measureSize + extraSkip) {
                enoughSpaceLeft = true;
                // FIXME
                measureSize += extraSkip;
            } else {
                // FIXME In this case, does the text automatically goes to the next line?
                enoughSpaceLeft = false;
            }
        } else {
            consecutiveRuby = false;
            enoughSpaceLeft = false;
            extraSkip = 0;
        }
    }

    @Override
    public int getSize(Paint paint, CharSequence text,
                       int start, int end, Paint.FontMetricsInt fm) {
        if (start >= end) {
            return 0;
        }
        measureRun(paint, text, start, end);
        return (int)measureSize;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        if (start >= end) {
            return;
        }



        final int oldColor = paint.getColor();
        if (useColor) {
            paint.setColor(color);
        }
        final boolean oldUnderline = paint.isUnderlineText();
        paint.setUnderlineText(underline);

        fm = paint.getFontMetrics();
        // FIXME hard coded constant, needs more testing
        final float offsetY = fm.ascent * 1.1f;

        /* avoid drawings outside the borders being clipped out
         * leftmost case
         */
        Rect bounds = new Rect();
        canvas.getClipBounds(bounds);  // canvas.left is 0

        spaceLeft = bounds.right - x - measureSize;

        // Translate the canvas to avoid overlapping with previous RubySpan.
        if (x + offsetX >= bounds.left && consecutiveRuby && enoughSpaceLeft) {
            canvas.save();
            canvas.translate(extraSkip, 0);
        }

        // Draw the text
        if (relativeLength == RelativeLength.TOO_LONG) {
            float step = 0.0f;
            if (end - start > 1) {
                step = widthDiff / (end - start - 1) + fontSize;
            }
            for (int i = start; i < end; ++i) {
                canvas.drawText(text, i, i + 1, x + step * (i - start), y, paint);
            }
        } else {
            canvas.drawText(text, start, end, x, y, paint);
        }

        // ruby don't need to be underlined
        paint.setUnderlineText(oldUnderline);

        // Translate the canvas to avoid drawing ruby outside the left border and thus
        // getting cut off. The reason to not translate the text in this case is it
        // will leads to many bugs.
        if (x + offsetX < bounds.left) {
            canvas.save();
            canvas.translate(-x - offsetX, 0);
        }

        // Draw the ruby
        paint.setTextSize(fontSize * FONTSIZE_SCALE);
        if (relativeLength == RelativeLength.TOO_SHORT) {
            float step = 0.0f;
            if (rubyLength > 1) {
                step = widthDiff / (rubyLength - 1) + rubyWidth / rubyLength;
            }
            for (int i = 0; i < rubyLength; ++i) {
                canvas.drawText(ruby, i, i + 1, x + offsetX + step * i, y + offsetY, paint);
            }
        } else {
            canvas.drawText(ruby, 0, rubyLength, x + offsetX, y + offsetY, paint);
        }
        paint.setTextSize(fontSize);

        if (x + offsetX < 0) {
            canvas.restore();
        }

        if (x + offsetX >= bounds.left && consecutiveRuby && enoughSpaceLeft) {
            canvas.restore();
        }
        paint.setColor(oldColor);
    }

}
