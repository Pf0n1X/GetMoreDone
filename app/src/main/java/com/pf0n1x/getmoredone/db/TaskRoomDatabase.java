package com.pf0n1x.getmoredone.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.pf0n1x.getmoredone.dao.AccountDao;
import com.pf0n1x.getmoredone.dao.TaskDao;
import com.pf0n1x.getmoredone.entities.Account;
import com.pf0n1x.getmoredone.entities.Task;
import com.pf0n1x.getmoredone.util.DateConverter;
import com.pf0n1x.getmoredone.util.TimeConverter;

@Database(entities = {Account.class, Task.class}, version = 4, exportSchema = false)
@TypeConverters({DateConverter.class, TimeConverter.class})
public abstract class TaskRoomDatabase extends RoomDatabase {

    // Data Members
    public abstract TaskDao taskDao();
    public abstract AccountDao accountDao();
    private static TaskRoomDatabase INSTANCE;

    public static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {

                    // Create database here.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class,
                            "task_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    /*
        Populate the database.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        // Data Members
        private final TaskDao mTaskDao;
        private final AccountDao mAccountDao;
        String[] tasks = {"Exercise", "Read a book", "Learn android"};

        PopulateDbAsync(TaskRoomDatabase db) {
            mTaskDao = db.taskDao();
            mAccountDao = db.accountDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created.
//            mTaskDao.deleteAll(); // TODO: Remove when no longer needed.

            // If we have no words, then create the initial list of words.
            if (mTaskDao.getAnyTask().length < 1) {
                for (int i = 0; i <= tasks.length - 1; i++) {
                    Task task = new Task(tasks[i],
                            null,
                            null,
                            null,
                            null,
                            null ,
                            null,
                            (i % 2 == 0));
                    mTaskDao.insert(task);
                }
            }

            return null;
        }
    }
}
