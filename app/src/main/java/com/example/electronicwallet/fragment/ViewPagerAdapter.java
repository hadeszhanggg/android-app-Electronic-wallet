package com.example.electronicwallet.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.electronicwallet.models.User;
import com.example.electronicwallet.models.Wallet;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private User user;
    private Wallet wallet;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, User user, Wallet wallet) {
        super(fragmentActivity);
        this.user = user;
        this.wallet=wallet;
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, User user, Wallet wallet) {
        super(fragmentManager, lifecycle);
        this.user = user;
        this.wallet=wallet;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return HomeFragment.newInstance(user, wallet);
            case 1:
                return DepositFragment.newInstance(user,wallet);
            case 2:
                return ChatFragment.newInstance(user);
            case 3:
                return PersonalFragment.newInstance(user,wallet);
            default:
                return HomeFragment.newInstance(user,wallet);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
