package com.example.myapplication;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.SystemClock;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.example.myapplication.Class.JMDict;
import com.example.myapplication.Class.JMDictHelper;
import com.example.myapplication.Class.JMDictParser;
import com.example.myapplication.Class.JMDictSAXParser;
import com.example.myapplication.Class.JMDictSQLiteHelper;
import com.example.myapplication.Class.JSONQuery;
import com.example.myapplication.Class.Kana;
import com.example.myapplication.Class.RubySpan;
import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextFragment extends Fragment {

    public Button pastingButton;
    public Button editButton;
    public Button kanaButton;
    public Button saveButton;
    public Button addVoc;
    public PopupWindow mPopupWindow;

    public ArrayList<Text> textList = new ArrayList<Text>();

    SharedPreferences sharedPref;
    SharedPreferences sharedPrefTextId;
    SharedPreferences.Editor editor;
    private JMDictSQLiteHelper helper;

    Kana kanaString = new Kana();
    public boolean focus = true;
    Tokenizer tokenizer = new Tokenizer() ;
    ClipboardManager clipboardManager;
    EditText pastText;
    EditText titleText;
    String actualToken;
    ViewPager2 viewPager2;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TextFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TextFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TextFragment newInstance(String param1, String param2) {
        TextFragment fragment = new TextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    public void onStart(){
        super.onStart();
        loadText(sharedPrefTextId.getString("textID", "0"));
    }
    public void onResume(){
        super.onResume();
        loadText(sharedPrefTextId.getString("textID", "0"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        View mainView = inflater.inflate(R.layout.activity_main,container,false);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView annotationText = popupView.findViewById(R.id.annotation_text);
        addVoc = (Button) popupView.findViewById(R.id.addvoc);
        pastText = (EditText) view.findViewById(R.id.textArea);
        titleText = (EditText) view.findViewById(R.id.title);
        pastingButton = (Button) view.findViewById(R.id.pastButton);
        editButton = (Button) view.findViewById(R.id.stopEdit);
        kanaButton = (Button) view.findViewById(R.id.kana);
        saveButton = (Button) view.findViewById(R.id.save);




        clipboardManager = (ClipboardManager) view.getContext().getSystemService(CLIPBOARD_SERVICE);
        sharedPref = getActivity().getSharedPreferences("MY_SHARED_PREF",Context.MODE_PRIVATE);
        sharedPrefTextId = getActivity().getSharedPreferences("TEXTID", MODE_PRIVATE);
        editor = sharedPref.edit();









        final ViewPager2 viewPager2 = mainView.findViewById(R.id.viewPager2);



        EditText screen = view.findViewById(R.id.textArea);
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("ok","detection");
                Log.d("Le swipe est il activé ?",Boolean.toString(viewPager2.isUserInputEnabled()));
                viewPager2.setUserInputEnabled(false);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText(sharedPrefTextId.getString("textID", "0"));


            }
        });


        kanaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cursor = 0;

                String textToFurigana = pastText.getText().toString();
                SpannableStringBuilder ssb = new SpannableStringBuilder(textToFurigana);
                List<Token> tokens = tokenizer.tokenize(textToFurigana);
                for (Token token : tokens) {
                    System.out.println(token.getSurface() +"        "+ kanaString.isKana(token.getSurface()) + "       " + token.getAllFeatures());
                    if(kanaString.isKana(token.getSurface())){
                        ssb.setSpan(new RubySpan(kanaString.kataToHira(token.getReading())),cursor,cursor+token.getSurface().length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                    }
                    cursor = cursor + token.getSurface().length();
                }
                pastText.setText(ssb);

            }
        });
        pastingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipData a = clipboardManager.getPrimaryClip();
                ClipData.Item item = a.getItemAt(0);
                String text = item.getText().toString();


                List<Token> tokens = tokenizer.tokenize(text);
                String newPastText = "";

                for (Token token : tokens) {

                    //System.out.println(token.getSurface() + "        "+ token.getReading() + "       "+ token.getPronunciation() + "       " + token.getAllFeatures());
                    newPastText = newPastText + token.getSurface() +"     " + kanaString.kataToHira( token.getPronunciation())+ token.getReading()+ "\n";

                }
                pastText.setText(text);



            }

        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(focus == true) {
                    pastText.setFocusable(false);
                    focus = false;
                    viewPager2.setUserInputEnabled(false);
                    SpannableString spannableString = new SpannableString(pastText.getText());
                    int start = 0;
                    int end = 0;
                    int cursor = 0;
                    List<Token> tokens = tokenizer.tokenize(pastText.getText().toString());

                    for (Token token : tokens) {
                        actualToken = token.getSurface();
                        end = start + token.getSurface().length();

                        //System.out.print("Token length value :");
                        //System.out.println(token.getSurface().length());
                        //System.out.print("End value :");
                        //System.out.println(end);
                        int finalCursor = cursor;

                        ClickableSpan clickableSpan = new MyClickableSpan() {
                            @Override
                            public void onClick(View view) {

                                int[] location = new int[2];

                                pastText.getLocationOnScreen(location);

                                int offset = pastText.getOffsetForPosition((int) view.getX(), (int) view.getY());
                                Layout layout = pastText.getLayout();
                                Rect bounds = new Rect();
                                int line = pastText.getLayout().getLineForOffset(finalCursor);
                                int lineStart = layout.getLineStart(line);
                                int lineEnd = layout.getLineEnd(line);
                                String lineText = layout.getText().subSequence(lineStart,lineEnd).toString();
                                pastText.getLineBounds(line, bounds);
                                int x = (int) (bounds.left+ location[0] + layout.getPrimaryHorizontal( lineText.lastIndexOf(token.getSurface())));
                                int y = bounds.top + location[1] + pastText.getCompoundPaddingTop();

                                mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y);
                                mPopupWindow.update(x, y-350, -1, -1);
                                System.out.println("position x : " + x+"      position y : "+ y);
                                System.out.println("position location x : " + location[0] + "Position location y :" + location[1]);
                                System.out.println("Line : " + line);
                                System.out.println("x layout : " + layout.getPrimaryHorizontal(lineStart  ));
                                String dictWord = token.getSurface() +"\n"+ JSONQuery.searchWord(getContext(),token.getSurface());
                                annotationText.setText(dictWord);

                                System.out.println("working I guess :/");
                                System.out.println(token.getSurface());

                            }
                        };

                        TextPaint textPaint = pastText.getPaint();

                        textPaint.setColor(Color.BLACK);
                        textPaint.setUnderlineText(false);

                        spannableString.setSpan(clickableSpan,cursor,cursor+token.getSurface().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        cursor = cursor + token.getSurface().length();
                        start = end + 1;

                    }
                    pastText.setText(spannableString);
                    pastText.setMovementMethod(LinkMovementMethod.getInstance());
                    pastText.setBackground(null);
                    pastText.setTextColor(Color.BLACK);
                    pastText.setHighlightColor(Color.TRANSPARENT);
                }
                else {
                    pastText.setFocusableInTouchMode(true);
                    focus = true;
                    mPopupWindow.dismiss();

                    TextPaint textPaint = pastText.getPaint();

                    textPaint.setColor(Color.BLACK);
                    textPaint.setUnderlineText(false);
                    pastText.setText(new SpannableString(pastText.getText()));
                    viewPager2.setUserInputEnabled(true);
                    pastText.setBackground(null);
                    pastText.setTextColor(Color.BLACK);
                    pastText.setHighlightColor(Color.TRANSPARENT);
                }

            }
        });
        addVoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                annotationText.getText();
            }
        });

        return view;
    }


    public void saveText(String textId) {

            String text = pastText.getText().toString();
            editor.putString(textId, text);
            editor.apply();
            String savedText = sharedPref.getString(textId, "Default");
            System.out.println(textId);
            System.out.println(savedText);
        }







    public void loadText(String textId) {

        String savedText = sharedPref.getString(textId, "");

        System.out.println("Ca marche ca marche");

        pastText.setText(savedText);
    }
    private void showPopupWindow(View view, int[] location) {
        // Set the annotation text for the clicked word
        TextView annotationText = mPopupWindow.getContentView().findViewById(R.id.annotation_text);
        annotationText.setText("Annotation for the clicked word");

        // Show the PopupWindow at the x and y position of the clicked word
        mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
    }

    public void addText(){

    }
    public void setEditText(String text) {
        pastText.setText(text);
    }


    public String getDefinition(String word) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("jmdict", new String[]{"definition"}, "kanji = ?", new String[]{word}, null, null, null);
        String definition = null;
        if (cursor.moveToFirst()) {
            definition = cursor.getString(0);
            Log.d("Definition", definition);
        }
        cursor.close();
        db.close();
        return definition;
    }
    public void showData(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("jmdict", new String[]{"kanji", "definition"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String kanji = cursor.getString(0);
            String definition = cursor.getString(1);
            Log.d("Data", "kanji: " + kanji + " , definition: " + definition);
        }
        cursor.close();
        db.close();
    }

    public void simulateSwipeLeft(View view) {
        // Get a reference to the view


        // Define the start and end points for the swipe
        int startX = view.getLeft();
        int startY = view.getTop();
        int endX = view.getLeft() - view.getWidth();
        int endY = view.getTop();

        // Create a MotionEvent object for the start of the swipe
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = startX;
        float y = startY;
        int metaState = 0;
        MotionEvent downEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, metaState);

        // Create a MotionEvent object for the end of the swipe
        long upTime = eventTime + 1000;
        x = endX;
        y = endY;
        MotionEvent upEvent = MotionEvent.obtain(downTime, upTime, MotionEvent.ACTION_UP, x, y, metaState);

        // Send the touch events to the view
        view.dispatchTouchEvent(downEvent);
        view.dispatchTouchEvent(upEvent);
    }
}