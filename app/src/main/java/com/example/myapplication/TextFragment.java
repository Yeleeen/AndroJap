package com.example.myapplication;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.constraintlayout.motion.widget.Debug;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
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
    public boolean focus = true;
    ClipboardManager clipboardManager;
    EditText pastText;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        View mainView = inflater.inflate(R.layout.activity_main,container,false);
        pastText = (EditText) view.findViewById(R.id.textArea);
        View zoneText = view.findViewById(R.id.scrollView);
        pastingButton = (Button) view.findViewById(R.id.pastButton);
        editButton = (Button) view.findViewById(R.id.stopEdit);
        kanaButton = (Button) view.findViewById(R.id.kana);
        clipboardManager = (ClipboardManager) view.getContext().getSystemService(CLIPBOARD_SERVICE);
        Bitmap bitmap = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);


        viewPager2 = mainView.findViewById(R.id.viewPager2);

        EditText screen = view.findViewById(R.id.textArea);
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ok","detection");
                Log.d("Le swipe est il activé ?",Boolean.toString(viewPager2.isUserInputEnabled()));
                viewPager2.setUserInputEnabled(false);
            }
        });

        kanaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Layout layout = pastText.getLayout();
                if (layout == null) {
                }
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(20);

                Canvas canvas = new Canvas(bitmap);
                canvas.drawPaint(paint);
                canvas.drawText("lalal",0,0,paint);
                int offset = 0;
                int lineOfText = layout.getLineForOffset(5);
                int xCoordinate = (int) layout.getPrimaryHorizontal(offset);
                int yCoordinate = layout.getLineTop(lineOfText);
                System.out.println(xCoordinate);
                System.out.println(yCoordinate);
            }
        });
        pastingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipData a = clipboardManager.getPrimaryClip();
                ClipData.Item item = a.getItemAt(0);
                String text = item.getText().toString();
                Tokenizer tokenizer = new Tokenizer() ;
                Kana kanaString = new Kana();
                List<Token> tokens = tokenizer.tokenize("お寿司が食べたい。");
                String newPastText = "";
                for (Token token : tokens) {
                    System.out.println(token.getSurface() + "\t" + token.getAllFeatures());
                    newPastText = newPastText + token.getSurface() +"     " + kanaString.kataToHira( token.getPronunciation())+ token.getReading()+ "\n";

                }
                pastText.setText(newPastText);



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
}