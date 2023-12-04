/*
Task realize all kinds of information that a to-do task should contain.
Also implement Parcelable for task flows between activities.
 */

package com.example.todo_app.utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Task implements Parcelable {
    private int id;
    private String title;
    private String content;
    private boolean completed;
    private String createdTime;
    private String expectedTime;

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }

    private String completedTime;

    public Task() {
        createdTime = "";
        expectedTime = "";
    }

    public Task(int id, String title, String content, boolean completed, String createdTime, String expectedTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.completed = completed;
        this.createdTime = createdTime;
        this.expectedTime = expectedTime;
    }

    protected Task(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        completed = in.readByte() != 0;
        createdTime = in.readString();
        expectedTime = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", completed='" + completed + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", expectedTime='" + expectedTime + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.content);
        parcel.writeBoolean(this.completed);
        parcel.writeString(this.createdTime);
        parcel.writeString(this.expectedTime);
    }
}
