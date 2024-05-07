package com.example.electronicwallet.models;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataModel extends ViewModel {
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<Wallet> walletLiveData = new MutableLiveData<>();

    public void setUser(User user) {
        userLiveData.setValue(user);
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }

    public void setWallet(Wallet wallet) {
        walletLiveData.setValue(wallet);
    }

    public LiveData<Wallet> getWallet() {
        return walletLiveData;
    }
}
