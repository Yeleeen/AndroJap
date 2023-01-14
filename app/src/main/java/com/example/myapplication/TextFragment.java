package com.example.myapplication;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.constraintlayout.motion.widget.Debug;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.SystemClock;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.example.myapplication.Class.Kana;
import com.example.myapplication.Class.RubySpan;


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

    public ArrayList<Text> textList = new ArrayList<Text>();

    SharedPreferences sharedPref;
    SharedPreferences sharedPrefTextId;
    SharedPreferences.Editor editor;

    Kana kanaString = new Kana();
    public boolean focus = true;
    Tokenizer tokenizer = new Tokenizer() ;
    ClipboardManager clipboardManager;
    EditText pastText;
    EditText titleText;
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
    public void onStart()
    {
        super.onStart();
        loadText(sharedPrefTextId.getString("textID", "0"));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadText(sharedPrefTextId.getString("textID", "0"));
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
                }
                else {
                    pastText.setFocusableInTouchMode(true);
                    focus = true;
                    viewPager2.setUserInputEnabled(true);
                }

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
<<<<<<< HEAD
        String savedText = sharedPref.getString(textId, "");
=======
        String savedText = sharedPref.getString(textId, "Default");
        System.out.println("Ca marche ca marche");
>>>>>>> df5e1ccbe72a6276b1b9a46060991d64be3a7cdc
        pastText.setText(savedText);
    }

    public void addText(){

    }
    public void setEditText(String text) {
        pastText.setText(text);
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