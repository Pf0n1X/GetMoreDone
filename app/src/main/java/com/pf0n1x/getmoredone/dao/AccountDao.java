package com.pf0n1x.getmoredone.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.pf0n1x.getmoredone.entities.Account;
import java.util.List;

@Dao
public interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Account account);

    @Query("DELETE FROM account_table")
    void deleteAll();

    @Query("SELECT * from account_table ORDER BY user_name ASC")
    LiveData<List<Account>> getAllAccounts();

    @Query("SELECT * from account_table LIMIT 1")
    Account[] getAnyAccount();

    @Delete
    void deleteAccount(Account account);

    @Update
    void update(Account... accounts);
}
