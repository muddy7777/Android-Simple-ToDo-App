/*
RecyclerView的Adapter，RecyclerView用于展示所有的Task
 */

package com.example.todo_app.addTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.todo_app.utils.Task;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Task> mTaskList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private TaskDatabaseHelper mTaskDatabaseHelper;

    public MyAdapter(Context context, List<Task> mTaskList){
        this.mTaskList = mTaskList;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mTaskDatabaseHelper = new TaskDatabaseHelper(mContext);
    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_task_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        //i = holder.getAdapterPosition();            //update position
        Task task = mTaskList.get(i);
        holder.mCbCompleted.setChecked(task.isCompleted());
        holder.mTvTitle.setText(task.getTitle());
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
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
        holder.mCbCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setCompleted(holder.mCbCompleted.isChecked());
                mTaskDatabaseHelper.updateTask(task);
            }
        });
    }

    public void refreshData(List<Task> newTaskList){
        this.mTaskList = newTaskList;
        notifyDataSetChanged();
    }

    private int dp2px(int dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCbCompleted;
        TextView mTvTitle;
        RelativeLayout rlContainer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mCbCompleted = itemView.findViewById(R.id.cb_completed);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }
}
