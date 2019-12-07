package com.pf0n1x.getmoredone.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.pf0n1x.getmoredone.entities.Task;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Query("SELECT * FROM task_table ORDER BY is_done DESC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE :dt BETWEEN start_date AND end_date ORDER BY is_done DESC")
    LiveData<List<Task>> getTasksOfDate(Date dt);

    @Query("SELECT * from task_table LIMIT 1")
    Task[] getAnyTask();

    @Delete
    void deleteWord(Task task);

    @Update
    void update(Task... tasks);
}
