package com.example.todo_app.utils;

import androidx.annotation.Nullable;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {

    @Override
    public int compare(Task task1, Task task2) {
        if(task1.getCreatedTime() == null || task2.getCreatedTime() == null) {
            return 0;
        }
        return task2.getCreatedTime().compareTo(task1.getCreatedTime());
    }

}
