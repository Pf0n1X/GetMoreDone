package com.pf0n1x.getmoredone.repositories;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.pf0n1x.getmoredone.dao.AccountDao;
import com.pf0n1x.getmoredone.db.TaskRoomDatabase;
import com.pf0n1x.getmoredone.entities.Account;

import java.util.List;

public class AccountRepository {

    // Data Members
    private AccountDao mAccountDao;
    private LiveData<List<Account>> mAllAccounts;

    // Constructors
    public AccountRepository(Application application) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        mAccountDao = db.accountDao();
        mAllAccounts = mAccountDao.getAllAccounts();
    }

    public LiveData<List<Account>> getAllAccounts() {
        return this.mAllAccounts;
    }

    public void insert(Account Account) {
        new insertAsyncAccount(mAccountDao).execute(Account);
    }

    public void deleteAll() {
        new deleteAllAsyncAccount(mAccountDao).execute();
    }

    public void delete(Account Account) {
        new deleteAsyncAccount(mAccountDao).execute(Account);
    }

    public void update(Account Account) {
        new updateAsyncAccount(mAccountDao).execute(Account);
    }

    // Sub Classes
    /*
        This class inserts Accounts asynchronously.
     */
    private static class insertAsyncAccount extends AsyncTask<Account, Void, Void> {

        // Data Members
        private AccountDao mAsyncAccountDao;

        // Constructors
        public insertAsyncAccount(AccountDao dao) {
            this.mAsyncAccountDao = dao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            mAsyncAccountDao.insert(accounts[0]);

            return null;
        }
    }

    /*
        This class deletes all Accounts asynchronously.
     */
    private static class deleteAllAsyncAccount extends AsyncTask<Account, Void, Void> {

        // Data Members
        private AccountDao mAsyncAccountDao;

        // Constructors
        public deleteAllAsyncAccount(AccountDao dao) {
            this.mAsyncAccountDao = dao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            mAsyncAccountDao.deleteAll();

            return null;
        }
    }

    /*
        This class deletes Accounts asynchronously.
     */
    private static class deleteAsyncAccount extends AsyncTask<Account, Void, Void> {

        // Data Members
        private AccountDao mAsyncAccountDao;

        // Constructors
        public deleteAsyncAccount(AccountDao dao) {
            this.mAsyncAccountDao = dao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            mAsyncAccountDao.deleteAccount(accounts[0]);

            return null;
        }
    }

    /*
        This class updates Accounts asynchronously.
     */
    private static class updateAsyncAccount extends AsyncTask<Account, Void, Void> {

        // Data Members
        private AccountDao mAsyncAccountDao;

        // Constructors
        public updateAsyncAccount(AccountDao dao) {
            this.mAsyncAccountDao = dao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            mAsyncAccountDao.update(accounts);

            return null;
        }
    }
}
