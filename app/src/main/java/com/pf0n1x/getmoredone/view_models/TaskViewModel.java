package com.pf0n1x.getmoredone.view_models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.pf0n1x.getmoredone.entities.Task;
import com.pf0n1x.getmoredone.repositories.TaskRepository;

import java.util.Date;
import java.util.List;

// TODO: Add documentation
public class TaskViewModel extends AndroidViewModel {

    // Data Members
    private TaskRepository mRepository;
    private LiveData<List<Task>> mTasks;

    // Constructors
    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TaskRepository(application);
//        mAllTasks = mRepository.getAllTasks();
    }

//    public LiveData<List<Task>> getAllTasks() {
//        return this.mAllTasks;
//    }
    public LiveData<List<Task>> getAllTasks() {
        this.mTasks = this.mRepository.getAllTasks();
        return this.mTasks;
    }

    public LiveData<List<Task>> getTasksOfDate(Date dt) {
        this.mTasks = this.mRepository.getTasksOfDate(dt);
        return this.mTasks;
    }

    public void insert(Task task) {
        mRepository.insert(task);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteTask(Task task) {
        mRepository.delete(task);
    }

    public void updateTask(Task task) {
        mRepository.update(task);
    }
}
