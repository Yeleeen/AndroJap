package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QCMFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QCMFragment extends Fragment {


    public Button launchQCM;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QCMFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QCMFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QCMFragment newInstance(String param1, String param2) {
        QCMFragment fragment = new QCMFragment();
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



        View view = inflater.inflate(R.layout.fragment_q_c_m, container, false);
        Button launchQCM = (Button) view.findViewById(R.id.QCM);
        // Inflate the layout for this fragment
        System.out.println("Fonctionne");


<<<<<<< HEAD


=======
>>>>>>> df5e1ccbe72a6276b1b9a46060991d64be3a7cdc

        launchQCM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QCM.class);
                QCMFragment.this.startActivity(intent);

                Intent intent = new Intent(getActivity(), QCM.class);
                QCMFragment.this.startActivity(intent);
                System.out.println("CLICK");

<<<<<<< HEAD

=======
>>>>>>> df5e1ccbe72a6276b1b9a46060991d64be3a7cdc

            }
        });

        return view;


    }
}