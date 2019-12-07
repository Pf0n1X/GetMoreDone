package com.pf0n1x.getmoredone.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pf0n1x.getmoredone.entities.Account;
import com.pf0n1x.getmoredone.repositories.AccountRepository;

import java.util.List;

// TODO: Add documentation
public class AccountViewModel extends AndroidViewModel {

    // Data Members
    private AccountRepository mRepository;
    private LiveData<List<Account>> mAllAccounts;

    // Constructors
    public AccountViewModel(@NonNull Application application) {
        super(application);
    }

    LiveData<List<Account>> getAllAccounts() {
        return mRepository.getAllAccounts();
    }

    public void insert(Account account) {
        mRepository.insert(account);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteAccount(Account account) {
        mRepository.delete(account);
    }

    public void updateAccount(Account account) {
        mRepository.update(account);
    }
}
