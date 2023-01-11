package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyFragmentManager extends FragmentStateAdapter {



    public MyFragmentManager(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new itemFragment();
        }
        if (position == 1){
            return new TextFragment();

        }
        else {
            return new QCMFragment();
        }


    }

    @Override
    public int getItemCount() {
        return 3;
    }
}