/*
The adapter of RecyclerView, dividing tasks into several groups such as the completed and not completed.
position: 0(type 0) -- 1 -- ... -- group[0].size()+1 --  group[0].size()+2(type 0) -- ... -- group[0].size()+group[1].size()+2

 */


package com.example.todo_app.addTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_app.DetailTask.DetailTaskActivity;
import com.example.todo_app.R;
import com.example.todo_app.taskDatabase.TaskDatabaseHelper;
import com.example.todo_app.utils.Group;
import com.example.todo_app.utils.Task;
import com.example.todo_app.utils.TaskComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Group> groupList;
    private List<Task> taskList;
    private Context mContext;

    public TaskDatabaseHelper getmTaskDatabaseHelper() {
        return mTaskDatabaseHelper;
    }

    private final TaskDatabaseHelper mTaskDatabaseHelper;

    public GroupAdapter(Context context) {
        this.mContext = context;
        this.mTaskDatabaseHelper = new TaskDatabaseHelper(mContext);
    }

    //return int, 0 when no task exists in database
    public int initData(){
        groupList = new ArrayList<>();
        taskList = mTaskDatabaseHelper.getAllTasks();
        taskList.sort(new TaskComparator());
        groupList = tasksToGroup(taskList);
        if(groupList.isEmpty()) {
            return 0;
        }else{
            return 1;
        }
    }

    public int updateFromDB(){
        taskList = mTaskDatabaseHelper.getAllTasks();
//        Collections.sort(taskList, new TaskComparator());
        groupList = tasksToGroup(taskList);
        notifyDataSetChanged();
        if(groupList.isEmpty()) {
            return 0;
        }else{
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_layout, parent, false);
            return new GroupViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task_layout, parent, false);
            return new TaskViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int count = 0;
        for (Group group : groupList) {
            int size = group.getTaskCount() + 1;
            if (position < count + size) {
                if (position == count) {
                    GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
                    groupViewHolder.tv_group.setText(group.getName());
                } else {
                    TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
                    Task task = group.getTasks().get(position - count - 1);
                    taskViewHolder.cb_Completed.setChecked(task.isCompleted());
                    taskViewHolder.tv_Title.setText(task.getTitle());
                    taskViewHolder.rlContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //to do detailTask
                            Intent intent = new Intent(mContext, DetailTaskActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("task", task); // 将Task对象转换为Bundle，并添加到Intent中
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    });
                    taskViewHolder.cb_Completed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            task.setCompleted(taskViewHolder.cb_Completed.isChecked());
                            mTaskDatabaseHelper.updateTask(task);
                            updateFromDB();
                        }
                    });
                }
                return;
            }
            count += size;
        }
        throw new IllegalArgumentException("Invalid position");
    }


    @Override
    public int getItemViewType(int position) {
        int count = 0;
        for (Group group : groupList) {
            int size = group.getTaskCount() + 1;
            if (position < count + size) {
                return position == count ? 0 : 1;
            }
            count += size;
        }
        throw new IllegalArgumentException("Invalid position");
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for(Group group:groupList){
            count += group.getTasks().size()+1;
        }
        return count;
    }

    List<Group> tasksToGroup(List<Task> TaskList){
        if(TaskList.isEmpty()){
            return new ArrayList<>();
        }
        Group completedGroup = new Group();
        Group notCompletedGroup = new Group();
        completedGroup.setName("已完成");
        notCompletedGroup.setName("未完成");
        completedGroup.setTasks(new ArrayList<>());
        notCompletedGroup.setTasks(new ArrayList<>());
        for(Task task:TaskList){
            if(task.isCompleted()){
                completedGroup.getTasks().add(task);
            }else {
                notCompletedGroup.getTasks().add(task);
            }
        }
        List<Group> GroupList = new ArrayList<>();
        GroupList.add(notCompletedGroup);
        GroupList.add(completedGroup);
        return GroupList;
    }

    int[] positionTransform(int itemPosition){ //all positions begin with 0
        int count = 0;
        int groupPosition = -1;
        int taskPosition = -1;
        for(Group group:groupList){
            groupPosition++;
            if(itemPosition<count + group.getTasks().size() + 1){
                taskPosition += (itemPosition-count);
                return new int[]{groupPosition, taskPosition};
            }
            count += group.getTasks().size()+1;
        }
        return null;
    }

    public void swapItems(int position, int targetPosition) {
        int[] result = positionTransform(position);
        int[] targetResult = positionTransform(targetPosition);
        if(result[0] == targetResult[0] && result[1] != -1 &&targetResult[1] != -1){
            int group = result[0];
            Collections.swap(groupList.get(group).getTasks(), result[1], targetResult[1]);
        }else{
            return;
        }
        notifyItemMoved(position, targetPosition);
    }

    public void removeItem(int position) {
        int[] result = positionTransform(position);
        int groupPosition = result[0];
        int taskPosition = result[1];
        if(taskPosition == -1){
            return;
        }else{
            mTaskDatabaseHelper.deleteTask(groupList.get(groupPosition).getTasks().get(taskPosition).getId());
            groupList.get(groupPosition).getTasks().remove(taskPosition);
        }
        notifyItemRemoved(position);
    }

    public void completeItem(int position) {
        int[] result = positionTransform(position);
        int groupPosition = result[0];
        int taskPosition = result[1];
        if(taskPosition == -1){
            return;
        }else{
            Task task = groupList.get(groupPosition).getTasks().get(taskPosition);
            task.setCompleted(true);
            mTaskDatabaseHelper.updateTask(task);
        }
        updateFromDB();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder{
      TextView tv_group;
      public GroupViewHolder(@NonNull View itemView) {
          super(itemView);
          this.tv_group = itemView.findViewById(R.id.tv_group);
      }
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox cb_Completed;
        TextView tv_Title;
        RelativeLayout rlContainer;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cb_Completed = itemView.findViewById(R.id.cb_completed);
            this.tv_Title = itemView.findViewById(R.id.tv_title);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }
}
