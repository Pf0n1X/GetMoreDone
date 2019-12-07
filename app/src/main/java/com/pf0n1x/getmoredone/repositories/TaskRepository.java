package com.pf0n1x.getmoredone.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pf0n1x.getmoredone.dao.TaskDao;
import com.pf0n1x.getmoredone.db.TaskRoomDatabase;
import com.pf0n1x.getmoredone.entities.Task;

import java.util.Date;
import java.util.List;

public class TaskRepository {

    // Data Members
    private TaskDao mTaskDao;
//    private LiveData<List<Task>> mAllTasks; // TODO: Delete this

    // Constructors
    public TaskRepository(Application application) {
        TaskRoomDatabase db = TaskRoomDatabase.getDatabase(application);
        mTaskDao = db.taskDao();
       //  mAllTasks = mTaskDao.getAllTasks(); // TODO: Delete this
    }

    public LiveData<List<Task>> getAllTasks() {
        return mTaskDao.getAllTasks();
    }

    public LiveData<List<Task>> getTasksOfDate(Date dt) {
        return this.mTaskDao.getTasksOfDate(dt);
    }

    public void insert(Task task) {
        new insertAsyncTask(mTaskDao).execute(task);
    }

    public void deleteAll() {
        new deleteAllAsyncTask(mTaskDao).execute();
    }

    public void delete(Task task) {
        new deleteAsyncTask(mTaskDao).execute(task);
    }

    public void update(Task task) {
        new updateAsyncTask(mTaskDao).execute(task);
    }

    // Sub Classes
    /*
        This class inserts Tasks asynchronously.
     */
    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {

        // Data Members
        private TaskDao mAsyncTaskDao;

        // Constructors
        public insertAsyncTask(TaskDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.insert(tasks[0]);

            return null;
        }
    }

    /*
        This class deletes all Tasks asynchronously.
     */
    private static class deleteAllAsyncTask extends AsyncTask<Task, Void, Void> {

        // Data Members
        private TaskDao mAsyncTaskDao;

        // Constructors
        public deleteAllAsyncTask(TaskDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.deleteAll();

            return null;
        }
    }

    /*
        This class deletes Tasks asynchronously.
     */
    private static class deleteAsyncTask extends AsyncTask<Task, Void, Void> {

        // Data Members
        private TaskDao mAsyncTaskDao;

        // Constructors
        public deleteAsyncTask(TaskDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.deleteWord(tasks[0]);

            return null;
        }
    }

    /*
        This class updates Tasks asynchronously.
     */
    private static class updateAsyncTask extends AsyncTask<Task, Void, Void> {

        // Data Members
        private TaskDao mAsyncTaskDao;

        // Constructors
        public updateAsyncTask(TaskDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            mAsyncTaskDao.update(tasks);

            return null;
        }
    }
}
