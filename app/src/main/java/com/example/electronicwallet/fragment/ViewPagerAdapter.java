package com.example.electronicwallet.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.electronicwallet.models.DataModel;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private DataModel dataViewModel;
    private User user;
    private Wallet wallet;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, DataModel viewModel) {
        super(fragmentActivity);
        dataViewModel = viewModel;
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, DataModel viewModel) {
        super(fragmentManager, lifecycle);
       dataViewModel = viewModel;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return ChatFragment.newInstance(user);
            case 2:
                return PersonalFragment.newInstance(user,wallet);
            default:
                return HomeFragment.newInstance();
        }
    }
    @Override
    public int getItemCount() {
        return 3;
    }
}
