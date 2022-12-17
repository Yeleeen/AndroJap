package com.example.myapplication.adaptaters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;


public class TextAdapter extends Fragment {

    static class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle, textPreview;

        NoteViewHolder(@NonNull View itemView) {

            super(itemView);

            textTitle = itemView.findViewById(R.id.textTitle);
            textPreview = itemView.findViewById(R.id.textPreview);

        }


    }
}
