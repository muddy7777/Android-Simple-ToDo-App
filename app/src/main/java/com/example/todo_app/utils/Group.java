package com.example.todo_app.utils;

import java.util.List;

public class Group {
    private String name;
    private List<Task> tasks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getTaskCount(){
        return tasks.size();
    }

}
