/*
应用的入口Activity，展示所有的Task
 */

package com.example.todo_app.addTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.todo_app.R;
import com.example.todo_app.utils.OnFragmentInteractionListener;
import com.example.todo_app.utils.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;


public class TaskActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    public FloatingActionButton mFloatingActionButton;
    private GroupAdapter mGroupAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayout mLinearLayout;
    private TextView tv_timeSelected;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar =new Toolbar(this);
        toolbar=findViewById(R.id.toolBar);
        initView();
        initEvent();
        switch (mGroupAdapter.initData()){
            case 0:{
                mLinearLayout.setVisibility(View.VISIBLE);
                break;
            }
            default:{
                mLinearLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshDataFromDB();
    }

    private void initEvent() {
//        mMyAdapter = new MyAdapter(this, mTaskList);
        mGroupAdapter = new GroupAdapter(this);
        mRecyclerView.setAdapter(mGroupAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3);
//        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        ItemTouchHelper.Callback callback = new MyCallback(mGroupAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void initView() {
        mFloatingActionButton = findViewById(R.id.addButton);
        mRecyclerView = findViewById(R.id.rv);
        mLinearLayout = findViewById(R.id.ll_noTask);
        task = new Task();
    }

    private void refreshDataFromDB(){
        switch (mGroupAdapter.updateFromDB()){
            case 0:{
                mLinearLayout.setVisibility(View.VISIBLE);
                break;
            }
            default:{
                mLinearLayout.setVisibility(View.GONE);
            }
        }
    }

    public void onClickAddButton(View view) {
        mFloatingActionButton.setVisibility(View.INVISIBLE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fcv, addTaskFragment.class, null, "addFragment")
                .addToBackStack("mBackStack")    //把Fragment添加到栈中，执行返回操作时先返回Fragment而不是Activity
                .setReorderingAllowed(true)       //允许系统调整位置
                .commit();
    }


    @Override
    public void onFragmentExit() {
        mFloatingActionButton.setVisibility(View.VISIBLE);
    }

    //addTaskFragment confirm function to create a new task.
    public void onClickConfirm(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        addTaskFragment mAddTaskFragment = (addTaskFragment)fragmentManager.findFragmentByTag("addFragment");
        if(mAddTaskFragment!=null && !mAddTaskFragment.getEt_title().getText().toString().trim().equals("")){
            task.setTitle(mAddTaskFragment.getEt_title().getText().toString().trim());
            task.setContent(mAddTaskFragment.getEt_content().getText().toString().trim());
            task.setCompleted(false);
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            task.setCreatedTime(year + "-" + (month + 1) + "-" + dayOfMonth);
            mGroupAdapter.getmTaskDatabaseHelper().addTask(task);
        }
        fragmentManager.popBackStack();
        refreshDataFromDB();
    }

    public void onClickTimeSelectButton(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String selectedDate = i + "-" + (i1 + 1) + "-" + i2;
                        task.setExpectedTime(selectedDate);
                        tv_timeSelected = findViewById(R.id.tv_time_selected);
                        tv_timeSelected.setText(selectedDate);
                    }

                },
                year,
                month,
                dayOfMonth
        );
        datePickerDialog.show();
    }
}