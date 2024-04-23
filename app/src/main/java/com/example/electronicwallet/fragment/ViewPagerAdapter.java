package com.example.electronicwallet.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.electronicwallet.models.User;
public class ViewPagerAdapter extends FragmentStateAdapter {
    private User user;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, User user) {
        super(fragmentActivity);
        this.user = user;
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, User user) {
        super(fragmentManager, lifecycle);
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return HomeFragment.newInstance(user);
            case 1:
                return DepositFragment.newInstance(user);
            case 2:
                return ChatFragment.newInstance(user);
            case 3:
                return PersonalFragment.newInstance(user);
            default:
                return HomeFragment.newInstance(user);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
