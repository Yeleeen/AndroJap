package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.myapplication.databinding.FragmentListtestBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdaptertest extends RecyclerView.Adapter<MyItemRecyclerViewAdaptertest.ViewHolder> {

    private final List<PlaceholderItem> mValues;
    private Context mContext;

    public MyItemRecyclerViewAdaptertest(List<PlaceholderItem> items, Context context) {
        mValues = items;
        mContext = context;
    }
    public String retrieveSharedPreferencesData() {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);

        return sharedPreferences.getString("saved_text", "Default*");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentListtestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(holder.mItem);
                retrieveSharedPreferencesData();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);

                String myString = retrieveSharedPreferencesData();
                System.out.println(myString);
                System.out.println("Touch");

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentListtestBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}